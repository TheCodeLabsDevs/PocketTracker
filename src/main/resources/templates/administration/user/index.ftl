<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/table.ftl" as t/>

    <@template.head 'Episodes'/>
    <@template.body>
        <@b.row>
            <@b.col "col-8">
                <@b.h2 "admin.user.headline"/>
            </@b.col>
            <@b.col "col-4">
                <@b.button label="button.add" url="/users/administration/add" icon="fas fa-plus" classes="float-end"/>
            </@b.col>
        </@b.row>

        <@t.table id="users">
            <@t.head>
                <@t.headCell label="admin.user.table.username"/>
                <@t.headCell label="admin.user.table.role"/>
                <@t.headCell label="admin.user.table.actions"/>
            </@t.head>
            <@t.body>
                <#list users as user>
                    <@t.row>
                        <@t.cell value=user.name/>
                        <@t.content value=user.userRole/>
                        <@t.cell>
                            <@t.action icon="fas fa-pen" url="/users/administration/${user.id}/edit" />
                            <#if currentUser.name != user.name>
                                <@t.action icon="fas fa-trash" url="/users/administration/${user.id}/delete" />
                            </#if>
                        </@t.cell>
                    </@t.row>
                </#list>
            </@t.body>
        </@t.table>

    </@template.body>
</html>
