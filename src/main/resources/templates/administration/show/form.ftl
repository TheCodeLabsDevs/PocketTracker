<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>

<#macro baseDate show>
    <@b.h3 title="show.baseData"/>

    <@f.form name="show" url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@b.row>
            <#assign today = .now?string['yyyy-MM-dd']/>

            <@f.input label="show.name" name="name" value=show.name!""/>
            <@f.input label="show.firstAired" name="firstAired" value=show.getFirstAiredReadable()!today/>

            <@f.textarea label="show.description" name="description" value=show.description!"" size="col-12"/>

            <@f.select label="show.type" name="type" options=showTypes value=show.type/>
            <@f.switch label="show.finished" name="finished" value=show.finished!false/>

            <@f.submit classes="float-end"/>
        </@b.row>
    </@f.form>
</#macro>

<#macro images show>
    <@b.h3 title="show.images"/>

    <#if show.getBannerPath()??>
        <img src="<@s.url "/resources/" + show.getBannerPath()/>" class="w-50 mb-4"/>
    </#if>

    <@b.row>
        <@f.form name="banner" url="/show/${show.id}/edit/BANNER" multipart=true>
            <@f.file label="show.banner" name="image"/>
            <@f.submit classes="float-end"/>
        </@f.form>
    </@b.row>
    <@b.separator/>

    <#if show.getPosterPath()??>
        <img src="<@s.url "/resources/" + show.getPosterPath()/>" class="w-25 mb-4"/>
    </#if>

    <@b.row>
        <@f.form name="poster" url="/show/${show.id}/edit/POSTER" multipart=true>
            <@f.file label="show.poster" name="image"/>
            <@f.submit classes="float-end"/>
        </@f.form>
    </@b.row>
</#macro>

<#macro seasons show>
    <@b.h3 title="show.seasons"/>

</#macro>