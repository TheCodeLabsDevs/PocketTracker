<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/table.ftl" as t/>

<#macro baseDate season>
    <@b.h3 title="season.baseData"/>

    <@f.form name="season" url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@b.row>
            <#assign today = .now?string['yyyy-MM-dd']/>

            <@f.input label="season.name" name="name" value=season.name!""/>
            <@f.input label="season.number" name="number" value=season.number!""/>
            <@f.textarea label="show.description" name="description" value=season.description!"" size="col-12"/>

            <@f.submit classes="float-end"/>
        </@b.row>
    </@f.form>
</#macro>

<#macro episodes season episodes>
    <@b.row>
        <@b.col size="col-6">
            <@b.h3 title="season.episodes"/>
        </@b.col>
        <@b.col size="col-6">
            <@m.open label="season.episodes.add" modalId="addEpisodes" classes="float-end"/>
        </@b.col>
    </@b.row>

    <@m.modal id="addEpisodes">
        <@m.header "season.episodes.add"/>
        <@m.body>
            <@f.form name="addEpisode" url="/season/${season.id}/episode/add">
                <@f.input label="season.episodes.add.count" name="episodeCount" value="1"/>
            </@f.form>
        </@m.body>
        <@m.footer>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><@b.localize "button.cancel"/></button>
            <@f.submit label="button.add" form="addEpisode" col=false/>
        </@m.footer>
    </@m.modal>


    <@t.table id="episodes">
        <@t.head>
            <@t.headCell label="season.episodes.number"/>
            <@t.headCell label="season.episodes.name"/>
            <@t.headCell label="season.episodes.actions"/>
        </@t.head>
        <@t.body>
            <#list episodes as episode>
                <@t.row>
                    <@t.cell value="${episode.number}"/>
                    <@t.cell value="${episode.name}"/>
                    <@t.cell>
                        <@t.action icon="fas fa-pen" url="/episoe/${episode.id}/edit" />
                    </@t.cell>
                </@t.row>
            </#list>
        </@t.body>
    </@t.table>
</#macro>