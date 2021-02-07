<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

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

        <@c.card classes="my-4">
            <@c.body>
                <@form.baseDate show/>
            </@c.body>
        </@c.card>

        <#if show.id??>
            <@c.card classes="my-4">
                <@c.body>
                    <@form.images show/>
                </@c.body>
            </@c.card>

            <@c.card classes="my-4">
                <@c.body>
                    <@form.seasons show/>
                </@c.body>
            </@c.card>

            <@c.card classes="my-4">
                <@c.body>
                    <@form.showDelete show/>
                </@c.body>
            </@c.card>
        </#if>
    </@template.body>
</html>
