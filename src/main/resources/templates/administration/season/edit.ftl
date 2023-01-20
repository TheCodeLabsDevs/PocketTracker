<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/card.ftl" as c/>

    <#import "form.ftl" as form/>

    <@template.head 'Episodes'/>
    <@template.body>
        <@b.flex>
            <@b.back_button url="/show/${show.id?c}/edit" showLabel=false/>
            <@b.button url="/season/${season.id?c}" label="season.view" classes="me-2"/>
            <@b.h2 title="${show.name} - ${season.name!''}" raw=true/>
        </@b.flex>

        <@c.card classes="my-4">
            <@c.body>
                <@form.baseDate season/>
            </@c.body>
        </@c.card>

        <@c.card classes="my-4">
            <@c.body>
                <@form.episodes season episodes/>
            </@c.body>
        </@c.card>
    </@template.body>
</html>
