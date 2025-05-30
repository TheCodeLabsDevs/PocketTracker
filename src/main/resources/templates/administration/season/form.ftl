<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/table.ftl" as t/>

<#import "/common/macros/deleteModal.ftl" as delete>

<#macro baseDate season>
    <@b.h3 title="season.baseData"/>

    <#assign objectName="season"/>
    <@f.form name=objectName url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@b.row>
            <@f.input objectName=objectName label="season.name" name="name" value=season.name!""/>
            <@f.input objectName=objectName label="season.number" name="number" value=season.number!""/>
            <@f.textarea objectName=objectName label="season.description" name="description" value=season.description!"" size="col-12"/>
            <@f.switch label="show.season.allDataFilled" name="filledCompletely" value=season.getFilledCompletely()/>
        </@b.row>
        <@b.row>
            <@b.col size="col-6">
                <@m.open label="season.delete" modalId="deleteSeason" buttonSize="btn" style="btn-danger" icon="fas fa-trash"/>
            </@b.col>
            <@f.submit classes="float-end" size="col-6" icon="fas fa-save"/>
        </@b.row>
    </@f.form>

    <@delete.modal modalId="deleteSeason" title="season.delete" deleteButton="season.delete" url="/season/${season.id}/delete">
        <@s.messageArgs code="season.delete.message" args=[season.getName()]/>
    </@delete.modal>
</#macro>

<#macro episodes season episodes>
    <@b.row>
        <@b.col size="col-6">
            <@b.h3 title="season.episodes"/>
        </@b.col>
        <@b.col size="col-6">
            <@m.open label="season.episodes.add" modalId="addEpisodes" classes="float-end" icon="fas fa-add"/>
        </@b.col>
    </@b.row>

    <@m.modal id="addEpisodes">
        <@m.header "season.episodes.add"/>
        <@m.body>
            <#assign objectName="addEpisode"/>
            <@f.form name=objectName url="/season/${season.id}/episode/add">
                <@f.input objectName=objectName label="season.episodes.add.count" name="episodeCount" value="1"/>
            </@f.form>
        </@m.body>
        <@m.footer>
            <@m.cancelButton/>
            <@f.submit label="button.add" form="addEpisode" col=false icon="fas fa-add"/>
        </@m.footer>
    </@m.modal>


    <@t.table id="episodes">
        <@t.head>
            <@t.headCell label="season.episodes.number"/>
            <@t.headCell label="season.episodes.name"/>
            <@t.headCell label="season.episodes.actions"/>
        </@t.head>
        <@t.body>
            <#list episodes?sort_by("number") as episode>
                <@t.row>
                    <@t.cell value="${episode.number}"/>
                    <@t.cell value="${episode.name}"/>
                    <@t.cell>
                        <@t.action icon="fas fa-pen" url="/episode/${episode.id}/edit" />

                        <#assign modalId = "deleteEpisode-${episode.id}">
                        <@m.openIcon icon="fas fa-trash" modalId=modalId classes="link-danger"/>
                        <@delete.modal modalId=modalId title="episode.delete" deleteButton="episode.delete" url="/episode/${episode.id}/delete">
                            <@s.messageArgs code="episode.delete.message" args=[episode.getName()]/>
                        </@delete.modal>
                    </@t.cell>
                </@t.row>
            </#list>
        </@t.body>
    </@t.table>
</#macro>