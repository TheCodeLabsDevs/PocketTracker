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
                    <#import "/common/macros/form.ftl" as f/>
                    <#import "/common/macros/card.ftl" as c/>

                    <@b.row>
                        <@b.col "col-8">
                            <@b.h2 "Neuer Benutzer"/>
                        </@b.col>
                    </@b.row>

                    <@f.form name="user" url="${springMacroRequestContext.contextPath}">
                        <@c.card>
                            <@c.body>
                                <@b.row>
                                    <@f.input label="Username" name="username"/>
                                    <@f.select label="Role" name="userType" options=userTypes/>

                                    <@f.input label="Password" name="password" type="password"/>
                                    <@f.input label="Paaword Repeat" name="passwordRepeat" type="password"/>

                                    <@f.submit "Submit"/>
                                </@b.row>
                            </@c.body>
                        </@c.card>
                    </@f.form>
                </div>
            </div>
        </main>
    </body>
</html>
