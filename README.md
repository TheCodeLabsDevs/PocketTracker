# PocketTracker

Simply track your watched TV shows and episodes, as well as audio plays and podcasts with PocketTracker

**Screenshot**

## Key Features

### Multi-user support

Multiple users can track their watched episodes by using different accounts.

### Progress visualization

Your number of watched episodes per season and show will be displayed as progress bars.

### TV show database

Add shows with seasons and episodes and fill them with all the information you need:

#### Shows

- name
- description
- first aired date
- banner image
- poster image
- status (mark as ended)

#### Seasons

- name
- number
- description

#### Episodes

- name
- number
- description
- first aired date
- duration
- image

:warning: **PocketTracker does not come with any data for shows, seasons and episodes. You have to fill in the data by
yourself.**

### Track your watched episodes

Mark episodes as watched to keep track of where you can continue watching in the season.

### Statistics

Get some interesting statistics about your watch behaviour:

- number of shows completely watched
- number of seasons completely watched
- number of episodes watched
- total watch time

### Dislike shows

If you have watched a few episodes of a show, but the show does not convince you, you can dislike it, so that it no
longer appears on your home page.

### Track radio plays and podcasts

A show can be marked as audio show to track radio plays or podcasts. The total play time of shows of type "audio" will
be summed up in the statistics.

### Backup & Restore

You can schedule an automatic backup including the PocketTracker database and all uploaded images. A backup can be
restored later.

## Available Languages

- English
- German

## Run PocketTracker

1. Build the source code: PocketTracker is a maven project, to build it use run `mvn package -f pom.xml`
2. Configure your instance:
   1. Create a new empty folder named `config` 
   2. Copy the file called `application.properties.example` into it and rename it to `application.properties`
   3. Adjust the `application.properties` file to your needs (All lines starting
   with `spring.security.oauth2.client.registration.gitlab` are optional)
3. Adjust the line volume mounts int the provided `docker-compose.yaml` to match your file paths
4. Run the provided `docker-compose.yaml` file with `docker-compose up -d`

