<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/table.ftl" as t/>

<#import "/common/macros/deleteModal.ftl" as delete>

<#macro baseDate episode>
    <@b.h3 title="episode.baseData"/>

    <@f.form name="episode" url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@b.row>
            <#assign today = .now?string['yyyy-MM-dd']/>

            <@f.input label="episode.name" name="name" value=episode.name!""/>
            <@f.input label="episode.number" name="number" value=episode.number!""/>
            <@f.textarea label="episode.description" name="description" value=episode.description!"" size="col-12"/>

            <@f.input label="episode.firstAired" name="firstAired" value=episode.getFirstAiredReadable()!today/>
            <@f.input label="episode.lengthInMinutes" name="lengthInMinutes" value=episode.lengthInMinutes!""/>

        </@b.row>
        <@b.row>
            <@b.col size="col-6">
                <@m.open label="episode.delete" modalId="deleteEpisode" buttonSize="btn" style="btn-danger"/>
            </@b.col>
            <@f.submit classes="float-end" size="col-6"/>
        </@b.row>
    </@f.form>

    <@delete.modal modalId="deleteEpisode" title="episode.delete" deleteButton="episode.delete" url="/episode/${episode.id}/delete">
        <@s.messageArgs code="episode.delete.message" args=[episode.getName()]/>
    </@delete.modal>
</#macro>

<#macro images episode>
    <@b.h3 title="show.images"/>

    <#if episode.getPosterPath()??>
        <img src="<@s.url "/resources/" + episode.getPosterPath()/>" class="w-25 mb-4"/>
    </#if>

    <@b.row>
        <@f.form name="poster" url="/episode/${episode.id}/edit/POSTER" multipart=true>
            <@f.file label="show.poster" name="image"/>
            <@f.submit classes="float-end"/>
        </@f.form>
    </@b.row>
</#macro>