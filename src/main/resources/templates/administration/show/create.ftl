<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/card.ftl" as c/>

    <#import "form.ftl" as form/>

    <@template.head 'Episodes'/>
    <@template.body>
        <@b.h2 title="show.create"/>

        <@c.card>
            <@c.body>
                <@form.form show/>
            </@c.body>
        </@c.card>

        <@c.card>
            <@c.body>

            </@c.body>
        </@c.card>
    </@template.body>
</html>
