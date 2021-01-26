from datetime import datetime

import psycopg2
import sqlite3

SQLITE_DATABASE_PATH = 'episodes.db'

POSTGRES_DATABASE_NAME = 'pockettracker'
POSTGRES_USER_NAME = 'pockettracker'
POSTGRES_PASSWORD = 'pocketTracker'
POSTGRES_HOST = '127.0.0.1'
POSTGRES_PORT = '5432'


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
        print(f'Migrating show {idx + 1}/{len(shows)} - {show["name"]}...')

        name = show['name']
        description = show['overview']
        firstAired = datetime.fromtimestamp(show['first_aired'])
        firstAired = datetime.strftime(firstAired, '%Y-%m-%d')
        showType = 0 if show['isTvShow'] == 1 else 1

        imageName = escape_image_path(name.lower())
        bannerPath = f'banner/{imageName}'
        posterPath = f'poster/{imageName}'

        newId = 1
        idAlreadyExists = True

        while idAlreadyExists:
            try:
                cursorPostgres.execute(
                    'INSERT INTO show (id, name, description, first_aired, type, banner_path, poster_path) '
                    'VALUES (%s, %s, %s, %s, %s, %s, %s);',
                    (newId, name, description, firstAired, showType, bannerPath, posterPath))
                idAlreadyExists = False
                print(f'newId: {newId}')
            except psycopg2.errors.UniqueViolation:
                idAlreadyExists = True
                newId += 1

    print('>>> Migrating shows DONE')

def escape_image_path(name: str) -> str:
    return ''.join([x if x.isalnum() else "_" for x in name])


if __name__ == '__main__':
    connectionSqlite = sqlite3.connect(SQLITE_DATABASE_PATH)
    connectionSqlite.row_factory = dict_factory
    cursorSqlite = connectionSqlite.cursor()

    connectionPostgres = psycopg2.connect(database=POSTGRES_DATABASE_NAME, user=POSTGRES_USER_NAME,
                                          password=POSTGRES_PASSWORD,
                                          host=POSTGRES_HOST, port=POSTGRES_PORT)
    connectionPostgres.autocommit = True
    cursorPostgres = connectionPostgres.cursor()

    try:
        migrate_shows()
    finally:
        connectionSqlite.close()
        connectionPostgres.close()
