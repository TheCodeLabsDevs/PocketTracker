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
            <@b.back_button url="/season/${season.id}/edit" showLabel=false/>
            <@b.button url="/episode/${episode.id}" label="episode.view" classes="me-2"/>
            <@b.h2 title="${episode.name!''}" raw=true/>
        </@b.flex>

        <@c.card classes="my-4">
            <@c.body>
                <@form.baseDate episode/>
            </@c.body>
        </@c.card>
    </@template.body>
</html>
