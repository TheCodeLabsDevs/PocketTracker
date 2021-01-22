<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/macros/base.ftl" as b/>
    <#import "/common/macros/card.ftl" as c/>
    <#import "/common/macros/form.ftl" as f/>
    <#import "/users/administration/form.ftl" as form>

    <@template.head 'Episodes'/>
    <@template.body>

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
    </@template.body>
</html>
