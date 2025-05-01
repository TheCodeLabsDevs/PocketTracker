<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/card.ftl" as c/>

    <#import "form.ftl" as form/>

    <#assign title>
        <#if movie.name?has_content>${movie.name}<#else><@b.localize "movie.create"/></#if>
    </#assign>

    <@template.head 'Episodes'>
      <script src="<@s.url "/js/importer/search.js"/>"></script>
    </@template.head>
    <@template.body>
        <@b.flex>
            <@b.back_button url=back_url showLabel=false/>
            <@b.h2 title=title raw=true/>
        </@b.flex>

        <@c.card classes="my-4">
            <@c.body>
                <@form.baseDate movie/>
            </@c.body>
        </@c.card>

        <#if movie.id??>
            <@c.card classes="my-4">
                <@c.body>
                    <@form.images movie/>
                </@c.body>
            </@c.card>

            <@c.card classes="my-4">
                <@c.body>
                    <@form.apiIdentifiers movie/>
                </@c.body>
            </@c.card>

            <@c.card classes="my-4">
                <@c.body>
                    <@form.movieDelete movie/>
                </@c.body>
            </@c.card>
        </#if>
    </@template.body>
</html>
