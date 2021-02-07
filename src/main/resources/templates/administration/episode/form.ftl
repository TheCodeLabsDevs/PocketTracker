<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/table.ftl" as t/>

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

            <@f.submit classes="float-end"/>
        </@b.row>
    </@f.form>
</#macro>
