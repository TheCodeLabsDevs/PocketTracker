<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>

<#macro baseDate show>
    <@b.h3 title="show.baseData"/>

    <@f.form name="show" url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@b.row>
            <@f.input label="show.name" name="name" value=show.name!""/>
            <#assign today = .now?string['yyyy-MM-dd']/>
            <@f.input label="show.firstAired" name="firstAired" value=show.getFirstAiredReadable()!today/>

            <@f.textarea label="show.description" name="description" value=show.description!"" size="col-12"/>

            <@f.select label="show.type" name="type" options=showTypes value=show.type/>

            <@f.submit classes="float-end"/>
        </@b.row>
    </@f.form>
</#macro>

<#macro images show>
    <@b.h3 title="show.images"/>

</#macro>

<#macro seasons show>
    <@b.h3 title="show.seasons"/>

</#macro>