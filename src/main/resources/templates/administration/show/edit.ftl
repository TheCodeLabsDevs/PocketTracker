<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/card.ftl" as c/>

    <#import "form.ftl" as form/>

    <#assign title>
        <#if show.name?has_content>${show.name}<#else><@b.localize "show.create"/></#if>
    </#assign>

    <@template.head 'Episodes'/>
    <@template.body>
        <@b.flex>
            <@b.back_button url=back_url showLabel=false/>
            <@b.h2 title=title raw=true/>
        </@b.flex>

        <@c.card margin="my-4">
            <@c.body>
                <@form.baseDate show/>
            </@c.body>
        </@c.card>

        <@c.card margin="my-4">
            <@c.body>
                <@form.images show/>
            </@c.body>
        </@c.card>

        <@c.card margin="my-4">
            <@c.body>
                <@form.seasons show/>
            </@c.body>
        </@c.card>
    </@template.body>
</html>
