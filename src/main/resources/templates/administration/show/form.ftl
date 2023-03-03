<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/table.ftl" as t/>

<#import "/common/macros/deleteModal.ftl" as delete>

<#macro baseDate show>
    <@b.h3 title="show.baseData"/>

    <#assign objectName="show"/>
    <@f.form name=objectName url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@b.row>
            <#assign today = .now?string['yyyy-MM-dd']/>

            <@f.input objectName=objectName label="show.name" name="name" value=show.name!""/>
            <@f.input objectName=objectName label="show.firstAired" name="firstAired" value=show.getFirstAiredReadable()!today/>

            <@f.textarea objectName=objectName label="show.description" name="description" value=show.description!"" size="col-12"/>

            <@f.select objectName=objectName label="show.type" name="type" options=showTypes value=show.type/>
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

    <div class="d-flex flex-row align-items-center">
        <@b.col size="col-2"classes="px-3">
            <h4><@b.localize "show.banner"/></h4>
        </@b.col>
        <@b.col size="col-5">
            <@f.form name="poster" url="/show/${show.id?c}/edit/BANNER" multipart=true>
                <@b.row>
                    <@f.file label="" name="image" size="col-9" margin=""/>
                    <@f.submit size="col-3" margin=""/>
                </@b.row>
            </@f.form>
        </@b.col>
        <@b.col size="col-2"classes="px-3 text-center">
            <h4><@b.localize "or"/></h4>
        </@b.col>
        <@b.col size="col-3">
            <a class="btn btn-primary ml-4 <#if show.getApiIdentifiers()?size == 0>disabled</#if> buttonAddImage" role="button" data-url="<@s.url "/show/${show.id?c}/showImages/BANNER"/>">
                <@b.localize "show.banner.fromApi"/>
            </a>
        </@b.col>
    </div>

    <@b.separator/>

    <#if show.getPosterPath()??>
        <img src="<@s.url "/resources/" + show.getPosterPath()/>" class="w-25 mb-4"/>
    </#if>

    <div class="d-flex flex-row align-items-center">
        <@b.col size="col-2"classes="px-3">
            <h4><@b.localize "show.poster"/></h4>
        </@b.col>
        <@b.col size="col-5">
            <@f.form name="poster" url="/show/${show.id?c}/edit/POSTER" multipart=true>
                <@b.row>
                    <@f.file label="" name="image" size="col-9" margin=""/>
                    <@f.submit size="col-3" margin=""/>
                </@b.row>
            </@f.form>
        </@b.col>
        <@b.col size="col-2"classes="px-3 text-center">
            <h4><@b.localize "or"/></h4>
        </@b.col>
        <@b.col size="col-3">
            <a class="btn btn-primary ml-4 <#if show.getApiIdentifiers()?size == 0>disabled</#if> buttonAddImage" role="button" data-url="<@s.url "/show/${show.id?c}/showImages/POSTER"/>">
                <@b.localize "show.poster.fromApi"/>
            </a>
        </@b.col>
    </div>

    <div id="modal-container-add-image"></div>
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
            <#assign objectName="addSeason"/>
            <@f.form name=objectName url="/show/${show.id?c}/season/add">
                <@f.input objectName=objectName label="show.season.add.count" name="seasonCount" value="1"/>
            </@f.form>
        </@m.body>
        <@m.footer>
            <@m.cancelButton/>
            <@f.submit label="button.add" form="addSeason" col=false/>
        </@m.footer>
    </@m.modal>


    <@t.table id="seasons">
        <@t.head>
            <@t.headCell label="show.season.number"/>
            <@t.headCell label="show.season.name"/>
            <@t.headCell label="show.season.allDataFilled"/>
            <@t.headCell label="show.season.actions"/>
        </@t.head>
        <@t.body>
            <#list show.getSeasons() as season>
                <@t.row>
                    <@t.cell value="${season.number}"/>
                    <@t.cell value="${season.name}"/>
                    <@t.cell>
                        <#if season.getFilledCompletely()>
                            <i class="fas fa-check"></i>
                        </#if>
                    </@t.cell>

                    <@t.cell>
                        <@t.action icon="fas fa-pen" url="/season/${season.id?c}/edit" />

                        <#assign modalId = "deleteSeason-${season.id?c}">
                        <@m.openIcon icon="fas fa-trash" modalId=modalId classes="link-danger"/>
                        <@delete.modal modalId=modalId title="season.delete" deleteButton="season.delete" url="/season/${season.id}/delete">
                            <@s.messageArgs code="season.delete.message" args=[season.getName()]/>
                        </@delete.modal>
                    </@t.cell>
                </@t.row>
            </#list>
        </@t.body>
    </@t.table>
</#macro>

<#macro apiIdentifiers show>
    <@b.row>
        <@b.col size="col-6">
            <@b.h3 title="show.apiIdentifiers"/>
        </@b.col>
        <@b.col size="col-6">
            <@m.open label="show.apiIdentifiers.add" modalId="addApiIdentifier" classes="float-end"/>
        </@b.col>
    </@b.row>

    <@m.modal id="addApiIdentifier">
        <@m.header "show.apiIdentifiers.add"/>
        <@m.body>
            <#assign objectName="newApiIdentifier"/>
            <@f.form name=objectName url="/show/${show.id?c}/apiIdentifier/add">
                <@f.select objectName=objectName label="show.apiIdentifiers.type" name="type" options=apiConfigurationTypes value=apiConfigurationTypes[0]/>
                <@f.input objectName=objectName label="show.apiIdentifiers.identifier" name="identifier" value=""/>
            </@f.form>
        </@m.body>
        <@m.footer>
            <@m.cancelButton/>
            <@f.submit label="button.add" form="newApiIdentifier" col=false/>
        </@m.footer>
    </@m.modal>

    <@t.table id="seasons">
        <@t.head>
            <@t.headCell label="show.apiIdentifiers.type"/>
            <@t.headCell label="show.apiIdentifiers.identifier"/>
            <@t.headCell label="show.season.actions"/>
        </@t.head>
        <@t.body>
            <#list show.getApiIdentifiers() as apiIdentifier>
                <@t.row>
                    <@t.cell value="${apiIdentifier.type}"/>
                    <@t.cell value="${apiIdentifier.identifier}"/>
                    <@t.cell>
                        <#assign modalId = "deleteApiIdentifier-${apiIdentifier.id?c}">
                        <@m.openIcon icon="fas fa-trash" modalId=modalId classes="link-danger"/>
                        <@delete.modal modalId=modalId title="show.apiIdentifiers.delete" deleteButton="show.apiIdentifiers.delete" url="/show/${show.id?c}/apiIdentifier/delete/${apiIdentifier.id?c}">
                            <@s.messageArgs code="show.apiIdentifiers.delete.message" args=[apiIdentifier.getType()]/>
                        </@delete.modal>
                    </@t.cell>
                </@t.row>
            </#list>
        </@t.body>
    </@t.table>
</#macro>

<#macro showDelete show>
    <@b.row>
        <@b.col size="col-12">
            <@b.h3 title="show.delete"/>
        </@b.col>
        <@b.col size="col-12">
            <@m.open label="show.delete" modalId="deleteShow" style="btn-danger" buttonSize=""/>
        </@b.col>
    </@b.row>

    <@delete.modal modalId="deleteShow" title="show.delete" deleteButton="show.delete" url="/show/${show.id?c}/delete">
        <@s.messageArgs code="show.delete.message" args=[show.getName()]/>
    </@delete.modal>
</#macro>