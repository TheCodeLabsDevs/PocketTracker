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
                    <#import "/common/macros/card.ftl" as c/>
                    <#import "/common/macros/form.ftl" as f/>

                    <@b.h2 "Benutzer löschen"/>

                    <@f.form name="delete" url=springMacroRequestContext.contextPath>
                        <@c.card>
                            <@c.header user.name/>
                            <@c.body message="Soll der Nutzer wirklich gelöscht werden?">
                                <@b.back_button/>
                                <@f.submit label="Löschen" col=false/>
                            </@c.body>
                        </@c.card>
                    </@f.form>
                </div>
            </div>
        </main>
    </body>
</html>
