<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/head.ftl" as headMacros>
    <@headMacros.head 'Episodes'/>

    <body class="bg-light">
        <#import "/common/navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <div class="py-5">
                <div class="container">
                    <#import "/common/macros/base.ftl" as b/>
                    <#import "/common/macros/table.ftl" as t/>

                    <div class="row">
                        <div class="col-8">
                            <@b.h2 "Benutzerübersicht"/>
                        </div>
                        <div class="col-4">
                            <@b.button label="Hinzufügen" url="/users/administration/add" icon="fas fa-plus" classes="float-end"/>
                        </div>
                    </div>

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
                                    <@t.cell value=user.userType/>
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
                </div>
            </div>
        </main>
    </body>
</html>
