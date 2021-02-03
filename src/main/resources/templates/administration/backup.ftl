<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>

    <@template.head 'Backup'/>
    <@template.body>
        <@b.row>
            <@b.col "col-12">
                <@b.h2 "admin.user.headline"/>
            </@b.col>
        </@b.row>

        <@b.row>
            <@b.col "col-12">
                <@b.button label="button.export" url="/administration/backup/export" icon="fas fa-download"/>
            </@b.col>
        </@b.row>
    </@template.body>
</html>
