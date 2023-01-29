<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/form.ftl" as f/>
    <#import "/common/components/table.ftl" as t/>

    <@template.head 'Backup'/>
    <@template.body>
        <@b.row>
            <@b.col "col-12">
                <@b.h2 "menu.administration.batch.edit"/>
            </@b.col>
        </@b.row>

        <@b.row classes="mt-3">
            <@b.col "col-12">
                <@b.h3 "admin.batchEdit.episodeLength"/>
            </@b.col>

            <#assign objectName="batchEditEpisodeLength"/>
            <@f.form name=objectName url="/administration/batchEdit/episodeLength">
                <@f.input objectName=objectName label="admin.batchEdit.episodeLength.showId" name="showId"/>
                <@f.input objectName=objectName label="admin.batchEdit.episodeLength.lengthInMinutes" name="lengthInMinutes"/>
                <@f.submit icon="fas fa-layer-group" label="admin.batchEdit.episodeLength.submit"/>
            </@f.form>
        </@b.row>
    </@template.body>
</html>
