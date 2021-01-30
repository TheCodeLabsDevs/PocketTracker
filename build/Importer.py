from collections import defaultdict

import psycopg2.extras
import sqlite3
import sys
from dataclasses import dataclass
from datetime import datetime
from typing import List

SQLITE_DATABASE_PATH = 'episodes.db'

POSTGRES_DATABASE_NAME = 'pockettracker'
POSTGRES_USER_NAME = 'pockettracker'
POSTGRES_PASSWORD = 'pocketTracker'
POSTGRES_HOST = '127.0.0.1'
POSTGRES_PORT = '5432'

POCKET_TRACKER_USER_ID = 2


def progress(count, total, message=''):
    bar_len = 60
    filled_len = int(round(bar_len * count / float(total)))

    percents = round(100.0 * count / float(total), 1)
    bar = '#' * filled_len + '-' * (bar_len - filled_len)

    sys.stdout.write(f'[{bar}] {percents}% ... {message}\r')
    sys.stdout.flush()


def dict_factory(cursor, row):
    d = {}
    for idx, col in enumerate(cursor.description):
        d[col[0]] = row[idx]
    return d


def migrate_shows():
    print('>>> Migrating shows...')
    cursorSqlite.execute('SELECT * FROM shows;')
    shows = cursorSqlite.fetchall()

    for idx, show in enumerate(shows):
        progress(idx, len(shows), message=f'Migrating show {show["name"]}')

        name = show['name']
        description = show['overview']
        firstAired = datetime.fromtimestamp(show['first_aired'])
        firstAired = datetime.strftime(firstAired, '%Y-%m-%d')
        showType = 0 if show['isTvShow'] == 1 else 1

        imageName = escape_image_path(name.lower())
        bannerPath = f'banner/{imageName}.jpg'
        posterPath = f'poster/{imageName}.jpg'

        cursorPostgres.execute(
            'INSERT INTO show (name, description, first_aired, type, banner_path, poster_path) '
            'VALUES (%s, %s, %s, %s, %s, %s);',
            (name, description, firstAired, showType, bannerPath, posterPath))

    print('\n>>> Migrating shows DONE\n')
    return shows


@dataclass
class Episode:
    showId: int
    name: str
    description: str
    number: int
    seasonNumber: int
    firstAired: str or None
    watched: bool


def parse_episodes() -> List[Episode]:
    print('>>> Parsing episodes...')
    cursorSqlite.execute('SELECT * FROM episodes;')
    episodes = cursorSqlite.fetchall()

    parsedEpisodes = []
    for idx, episode in enumerate(episodes):
        progress(idx, len(episodes), message=f'Parsing episode {episode["name"]}')

        firstAired = episode['first_aired']
        if firstAired:
            firstAired = datetime.fromtimestamp(episode['first_aired'])
            firstAired = datetime.strftime(firstAired, '%Y-%m-%d')

        parsedEpisodes.append(Episode(episode['show_id'],
                                      episode['name'],
                                      episode['overview'],
                                      episode['episode_number'],
                                      episode['season_number'],
                                      firstAired,
                                      episode['watched']))

    print('\n>>> Parsing episodes DONE\n')
    return parsedEpisodes


def escape_image_path(name: str) -> str:
    return ''.join([x if x.isalnum() else "_" for x in name])


def migrate_seasons_for_show(episodeList):
    uniqueSeasonNumbers = {episode.seasonNumber for episode in episodeList}
    print(f'    Found the following unique season numbers: {uniqueSeasonNumbers}')

    for idx, seasonNumber in enumerate(uniqueSeasonNumbers):
        cursorPostgres.execute(
            'INSERT INTO season (name, description, number, show_id) '
            'VALUES (%s, %s, %s, %s) RETURNING id;',
            (f'Staffel {seasonNumber}', None, seasonNumber, newShow['id']))

        newSeasonId = cursorPostgres.fetchone()['id']
        print(f'\nSeason [{idx + 1}/{len(uniqueSeasonNumbers)}] Migrating episodes...')
        migrate_episodes_for_season(episodeList, seasonNumber, newSeasonId)


def migrate_episodes_for_season(allEpisodes, seasonNumber, newSeasonId):
    episodesForSeason = [episode for episode in allEpisodes if episode.seasonNumber == seasonNumber]
    for idx, episode in enumerate(episodesForSeason):
        progress(idx, len(episodesForSeason), message=f'[E{episode.number:02d}] {episode.name}')
        migrate_episode(episode, newSeasonId)


def migrate_episode(episode, newSeasonId):
    cursorPostgres.execute(
        'INSERT INTO episode (name, description, first_aired, length_in_minutes, number, season_id) '
        'VALUES (%s, %s, %s, %s, %s, %s) RETURNING id;',
        (episode.name, episode.description, episode.firstAired, None, episode.number, newSeasonId))

    if episode.watched:
        newSeasonId = cursorPostgres.fetchone()['id']
        migrate_watched_episode(newSeasonId)


def migrate_watched_episode(newEpisodeId):
    cursorPostgres.execute(
        'INSERT INTO appuser_watched_episodes (user_id, watched_episodes_id) VALUES (%s, %s);',
        (POCKET_TRACKER_USER_ID, newEpisodeId))

    if episode.watched:
        migrate_watched_episode(episode)


if __name__ == '__main__':
    connectionSqlite = sqlite3.connect(SQLITE_DATABASE_PATH)
    connectionSqlite.row_factory = dict_factory
    cursorSqlite = connectionSqlite.cursor()

    connectionPostgres = psycopg2.connect(database=POSTGRES_DATABASE_NAME, user=POSTGRES_USER_NAME,
                                          password=POSTGRES_PASSWORD,
                                          host=POSTGRES_HOST, port=POSTGRES_PORT,
                                          cursor_factory=psycopg2.extras.RealDictCursor)
    connectionPostgres.autocommit = True
    cursorPostgres = connectionPostgres.cursor()

    try:
        oldShows = migrate_shows()

        episodes = parse_episodes()
        episodesByShows = defaultdict(list)

        for episode in episodes:
            episodesByShows[episode.showId].append(episode)

        showIndex = 0
        for showId, episodeList in episodesByShows.items():
            showIndex += 1
            showForId = [show for show in oldShows if show['_id'] == showId][0]
            print(f'\n\n>>> [{showIndex}/{len(episodesByShows.keys())}] '
                  f'Migrating episodes for show "{showForId["name"]}"...')

            cursorPostgres.execute("SELECT * FROM show WHERE name = %s", (showForId["name"],))
            newShow = cursorPostgres.fetchone()
            print(f'    Found matching show in new database: {newShow}')

            migrate_seasons_for_show(episodeList)

            print('\n\n ##### DONE #####')
    finally:
        connectionSqlite.close()
        connectionPostgres.close()
