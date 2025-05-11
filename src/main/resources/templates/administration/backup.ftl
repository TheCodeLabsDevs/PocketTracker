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
                <h2> <i class="fas fa-download"></i> <@b.localize "menu.administration.backup"/></h2>
            </@b.col>
        </@b.row>

        <@b.row>
            <@b.col "col-12">
                <@b.button label="button.export" url="/administration/backup/export" icon="fas fa-download"/>
            </@b.col>
        </@b.row>

        <#if backups??>
            <@t.table id="backups">
                <@t.head>
                    <@t.headCell label="admin.backup.date"/>
                    <@t.headCell label="admin.backup.size"/>
                    <@t.headCell label="admin.backup.includeDatabase"/>
                    <@t.headCell label="admin.backup.includeImages"/>
                    <@t.headCell label="admin.backup.download"/>
                </@t.head>
                <@t.body>
                    <#list backups as backup>
                        <@t.row>
                            <@t.cell value=backup.getName()/>
                            <@t.cell value=backup.getSizeFormatted()/>
                            <@t.cell>
                                <#if backup.database()>
                                    &check;
                                <#else>
                                    &#10134;
                                </#if>
                            </@t.cell>
                            <@t.cell>
                                <#if backup.images()>
                                    &check;
                                <#else>
                                    &#10134;
                                </#if>
                            </@t.cell>
                            <@t.cell>
                                <@t.action icon="fas fa-download" url="/administration/backup/download/${backup.getPathName()}"/>
                            </@t.cell>
                        </@t.row>
                    </#list>
                </@t.body>
            </@t.table>
        </#if>

        <@b.separator/>

        <@b.row>
            <@b.col "col-12">
                <h2><i class="fas fa-upload"></i> <@b.localize "admin.backup.restore"/></h2>
            </@b.col>

            <@f.form name="clearForm" url="/administration/backup/clear" multipart=true>
                <@f.submit label="button.database.delete" classes="float-end" style="btn-danger" icon="fas fa-trash"/>
            </@f.form>

            <@f.form name="restoreForm" url="/administration/backup/restore" multipart=true>
                <@f.file label="admin.backup.upload" name="restore" accept="application/zip"/>
                <@f.submit label="button.restore" classes="float-end" style="btn-danger" icon="fas fa-upload"/>
            </@f.form>
        </@b.row>
    </@template.body>
</html>
