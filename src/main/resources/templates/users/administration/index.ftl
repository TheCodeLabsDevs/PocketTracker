<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/macros/base.ftl" as b/>
    <#import "/common/macros/table.ftl" as t/>

    <@template.head 'Episodes'/>
    <@template.body>
        <@b.row>
            <@b.col "col-8">
                <@b.h2 "Benutzerübersicht"/>
            </@b.col>
            <@b.col "col-4">
                <@b.button label="Hinzufügen" url="/users/administration/add" icon="fas fa-plus" classes="float-end"/>
            </@b.col>
        </@b.row>

        <@t.table id="users">
            <@t.head>
                <@t.headCell label="Username"/>
                <@t.headCell label="Role"/>
                <@t.headCell label="Actions"/>
            </@t.head>
            <@t.body>
                <#list users as user>
                    <@t.row>
                        <@t.cell value=user.name/>
                        <@t.cell value=user.userRole.toString()/>
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
