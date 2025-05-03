<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/modal.ftl" as m/>
<#import "/common/components/table.ftl" as t/>

<#import "/common/macros/deleteModal.ftl" as delete>

<#macro baseDate movie>
    <@b.h3 title="movie.baseData"/>

    <#assign objectName="movie"/>
    <@f.form name=objectName url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@b.row>
            <#assign today = .now?string['yyyy-MM-dd']/>

            <@f.input objectName=objectName label="movie.name" name="name" value=movie.name!"" size="col-12"/>
            <@f.input objectName=objectName label="movie.releaseDate" name="releaseDate" value=movie.getReleaseDateReadable()!today/>
            <@f.input objectName=objectName label="movie.lengthInMinutes" name="lengthInMinutes" value=movie.lengthInMinutes!""/>

            <@f.textarea objectName=objectName label="movie.description" name="description" value=movie.description!"" size="col-12"/>

            <@f.submit classes="float-end"/>
        </@b.row>
    </@f.form>
</#macro>

<#macro images movie>
    <@b.h3 title="movie.images"/>

    <#if movie.getPosterPath()??>
        <img src="<@s.url "/resources/" + movie.getPosterPath()/>" class="w-25 mb-4"/>
    </#if>

    <div class="d-flex flex-row align-items-center">
        <@b.col size="col-2"classes="px-3">
            <h4><@b.localize "movie.poster"/></h4>
        </@b.col>
        <@b.col size="col-5">
            <@f.form name="poster" url="/movie/${movie.id}/edit/POSTER" multipart=true>
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
            <a class="btn btn-primary ml-4 <#if movie.getApiIdentifiers()?size == 0>disabled</#if> buttonAddImage" role="button" data-url="<@s.url "/movie/${movie.id}/showImages/POSTER"/>">
                <@b.localize "show.poster.fromApi"/>
            </a>
        </@b.col>
    </div>

    <div id="modal-container-add-image"></div>
</#macro>

<#macro apiIdentifiers movie>
    <@b.row>
        <@b.col size="col-6">
            <@b.h3 title="show.apiIdentifiers"/>
        </@b.col>
        <@b.col size="col-6">
            <@m.open label="show.apiIdentifiers.add" modalId="addApiIdentifier" classes="float-end"/>
        </@b.col>
    </@b.row>

    <@m.modal id="addApiIdentifier" modalSize="modal-lg">
        <@m.header "show.apiIdentifiers.add"/>
        <@m.body>
            <#assign objectName="newApiIdentifier"/>
            <@b.row>
                <@f.select objectName=objectName label="show.apiIdentifiers.type" name="apiIdentifierType" options=apiConfigurationTypes value=apiConfigurationTypes[0]/>
                <@f.input objectName=objectName label="movie.apiIdentifiers.search" name="searchShowName" value="" url="movie/searchApi"/>
            </@b.row>

            <div id="searchResultContainer" data-form-url="/movie/${movie.id}/apiIdentifier/add"></div>
        </@m.body>
        <@m.footer>
            <@m.cancelButton/>
        </@m.footer>
    </@m.modal>

    <@t.table id="apiIdentifiers">
        <@t.head>
            <@t.headCell label="show.apiIdentifiers.type"/>
            <@t.headCell label="show.apiIdentifiers.identifier"/>
            <@t.headCell label="show.season.actions"/>
        </@t.head>
        <@t.body>
            <#list movie.getApiIdentifiers() as apiIdentifier>
                <@t.row>
                    <@t.cell value="${apiIdentifier.type}"/>
                    <@t.cell value="${apiIdentifier.identifier}"/>
                    <@t.cell>
                        <#assign modalId = "deleteApiIdentifier-${apiIdentifier.id}">
                        <@m.openIcon icon="fas fa-trash" modalId=modalId classes="link-danger"/>
                        <@delete.modal modalId=modalId title="show.apiIdentifiers.delete" deleteButton="show.apiIdentifiers.delete" url="/movie/${movie.id}/apiIdentifier/delete/${apiIdentifier.id}">
                            <@s.messageArgs code="show.apiIdentifiers.delete.message" args=[apiIdentifier.getType()]/>
                        </@delete.modal>
                    </@t.cell>
                </@t.row>
            </#list>
        </@t.body>
    </@t.table>
</#macro>

<#macro movieDelete movie>
    <@b.row>
        <@b.col size="col-12">
            <@b.h3 title="movie.delete"/>
        </@b.col>
        <@b.col size="col-12">
            <@m.open label="movie.delete" modalId="deleteMovie" style="btn-danger" buttonSize=""/>
        </@b.col>
    </@b.row>

    <@delete.modal modalId="deleteMovie" title="movie.delete" deleteButton="movie.delete" url="/movie/${movie.id}/delete">
        <@s.messageArgs code="movie.delete.message" args=[movie.getName()]/>
    </@delete.modal>
</#macro>