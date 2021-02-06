<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/table.ftl" as t/>

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
    <@b.row>
        <@b.col size="col-6">
            <@b.h3 title="show.seasons"/>
        </@b.col>
        <@b.col size="col-6">
            <@m.open label="show.season.add" modalId="addSeasons" classes="float-end"/>
        </@b.col>
    </@b.row>

    <@m.modal id="addSeasons">
        <@m.header "show.season.add"/>
        <@m.body>
            <@f.form name="addSeason" url="/show/${show.id}/season/add">
                <@f.input label="show.season.add.count" name="seasonCount" value="1"/>
            </@f.form>
        </@m.body>
        <@m.footer>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><@b.localize "button.cancel"/></button>
            <@f.submit label="button.add" form="addSeason" col=false/>
        </@m.footer>
    </@m.modal>


    <@t.table id="seasons">
        <@t.head>
            <@t.headCell label="show.season.number"/>
            <@t.headCell label="show.season.name"/>
            <@t.headCell label="show.season.actions"/>
        </@t.head>
        <@t.body>
            <#list show.getSeasons() as season>
                <@t.row>
                    <@t.cell value="${season.number}"/>
                    <@t.cell value="${season.name}"/>
                    <@t.cell>
                        <@t.action icon="fas fa-pen" url="/season/${season.id}/edit" />
                    </@t.cell>
                </@t.row>
            </#list>
        </@t.body>
    </@t.table>
</#macro>