import json
import os.path
import uuid
from typing import Any, Iterator, TypeVar

T = TypeVar('EntityWithId')


def convert_id_to_uuid(elements: list[T], displayName: str) -> Iterator[tuple[T, int, str]]:
    for element in elements:
        oldId = element['id']
        newId = str(uuid.uuid4())
        print(f'Convert {displayName} with id {oldId} to {newId}')
        element['id'] = newId

        yield element, oldId, newId


def convert(backupPath: str) -> dict[str, Any]:
    with open(backupPath, encoding='utf-8') as f:
        data = json.load(f)

    list(convert_id_to_uuid(data['apiConfigurations'], 'apiConfiguration'))

    for user, __, __ in convert_id_to_uuid(data['users'], 'user'):
        list(convert_id_to_uuid(user['authentications'], 'authentication'))
        list(convert_id_to_uuid(user['tokens'], 'token'))

    for show, oldId, newId in convert_id_to_uuid(data['shows'], 'show'):
        list(convert_id_to_uuid(show['apiIdentifiers'], 'apiIdentifier'))

        for user in data['users']:
            for userShow in user['shows']:
                if userShow['show'] == oldId:
                    userShow['show'] = newId
                    print(f'\t\t\tUpdate added show {newId} for user {user["name"]}')

        for season, __, __ in convert_id_to_uuid(show['seasons'], 'season'):
            for episode, oldEpisodeId, newEpisodeId in convert_id_to_uuid(season['episodes'], 'episode'):
                for user in data['users']:
                    for watchedEpisode in user['watched']:
                        if watchedEpisode['episode'] == oldEpisodeId:
                            watchedEpisode['episode'] = newEpisodeId
                            print(f'\t\t\tUpdate watched episode {newEpisodeId} for user {user["name"]}')

    return data


if __name__ == '__main__':
    BACKUP_FILE = r'database.json'
    convertedData = convert(BACKUP_FILE)

    filePath, extension = os.path.splitext(BACKUP_FILE)

    with open(f'{filePath}_converted{extension}', 'w', encoding='utf-8') as f:
        json.dump(convertedData, f, ensure_ascii=False)
