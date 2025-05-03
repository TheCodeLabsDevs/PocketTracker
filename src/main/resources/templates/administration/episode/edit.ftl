<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/card.ftl" as c/>

    <#import "form.ftl" as form/>

    <@template.head helpers.getShortCode(episode) + " - " + episode.getName()/>
    <@template.body>
        <@b.flex>
            <@b.back_button url="/season/${season.id}/edit" showLabel=false/>
            <@b.button url="/episode/${episode.id}" label="episode.view" classes="me-2"/>
            <@b.h2 title="${episode.name!''}" raw=true/>

            <div class="btn-group ms-auto" role="group">
                <#if previousEpisode??>
                    <@b.button url="/episode/${previousEpisode.id}/edit" icon="fas fa-arrow-left" label="button.previous" style="btn-outline-primary"/>
                </#if>
                <#if nextEpisode??>
                    <@b.button url="/episode/${nextEpisode.id}/edit" icon="fas fa-arrow-right" label="button.next" style="btn-outline-primary"/>
                </#if>
            </div>
        </@b.flex>

        <@c.card classes="my-4">
            <@c.body>
                <@form.baseDate episode/>
            </@c.body>
        </@c.card>

        <#if episode.id??>
            <@c.card classes="my-4">
                <@c.body>
                    <@form.images episode/>
                </@c.body>
            </@c.card>
        </#if>
    </@template.body>
</html>
