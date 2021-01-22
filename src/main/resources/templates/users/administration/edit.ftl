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
                    <#import "/users/administration/form.ftl" as form>

                    <@b.row>
                        <@b.col "col-8">
                            <@b.h2 "Benutzer bearbeiten"/>
                        </@b.col>
                    </@b.row>

                    <@form.form user userTypes/>
                </div>
            </div>
        </main>
    </body>
</html>
