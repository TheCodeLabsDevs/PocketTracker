<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/form.ftl" as f>
    <#import "/common/components/modal.ftl" as m>
    <#import "/common/macros/show.ftl" as showMacros/>

    <@template.head currentPage>
        <script src="<@s.url "/js/importer/search.js"/>"></script>
    </@template.head>
    <@template.body>
        <@b.row classes="mb-3">
            <@b.col classes="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                <#if isUserSpecificView>
                    <#if shows?size == userShows?size>
                        <h4><@s.messageArgs code="index.user.specific.number.of.shows" args=[userShows?size]/></h4>
                    <#else>
                        <h4><@s.messageArgs code="index.showing.number.of.shows" args=[shows?size, userShows?size]/></h4>
                    </#if>
                <#else>
                    <#if shows?size == numberOfAllShows>
                        <h4><@s.messageArgs code="index.number.of.shows" args=[numberOfAllShows]/></h4>
                    <#else>
                        <h4><@s.messageArgs code="index.showing.number.of.shows" args=[shows?size, numberOfAllShows]/></h4>
                    </#if>
                </#if>
            </@b.col>
        </@b.row>

        <#if !isUserSpecificView>
            <@b.hasPermission "ADMIN">
                <@b.row classes="mb-4">
                    <@b.col>
                        <div class="dropdown">
                            <a class="btn btn-sm btn-primary float-end dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <@b.localize "button.add"/>
                            </a>

                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="<@s.url "/show/create"/>"><@b.localize "button.add.manually"/></a></li>
                                <li><a class="dropdown-item" data-bs-toggle="modal" data-bs-target="#importApiIdentifierModal"><@b.localize "button.add.importer"/></a></li>
                            </ul>
                        </div>

                        <@m.modal id="importApiIdentifierModal" modalSize="modal-lg">
                            <@m.header "show.api.create"/>
                            <@m.body>
                                <#assign objectName="importApiIdentifier"/>
                                  <@b.row>
                                      <@f.select objectName=objectName label="show.apiIdentifiers.type" name="apiIdentifierType" options=apiConfigurationTypes value=apiConfigurationTypes[0]/>
                                      <@f.input objectName=objectName label="show.apiIdentifiers.search" name="searchShowName" value=""/>
                                  </@b.row>

                                <div id="searchResultContainer" data-form-url="/show/createFromApi"></div>
                            </@m.body>
                            <@m.footer>
                                <@m.cancelButton/>
                            </@m.footer>
                        </@m.modal>
                    </@b.col>
                </@b.row>
            </@b.hasPermission>
        </#if>

        <#if isUserSpecificView>
            <#assign objectName="sortOptionForm"/>
            <@f.form name=objectName url=springMacroRequestContext.getRequestUri() rawUrl=true>

                <div class="d-flex justify-content-end">
                    <a class="btn <#if showDislikedShows>btn-secondary<#else>btn-outline-secondary</#if> mb-4 me-2" role="button" id="showDislikedShows">
                        <#if showDislikedShows>
                            <i class="fas fa-thumbs-down"></i>
                        <#else>
                            <i class="far fa-thumbs-down"></i>
                        </#if>
                    </a>
                    <input type="hidden" name="showDislikedShows" value="${showDislikedShows?c}">

                    <@f.select objectName=objectName name="filterOption" options=showFilterOptions value=currentFilterOption.name() size="me-2"/>
                    <@f.select objectName=objectName name="sortOption" options=showSortOptions value=currentSortOption.name() size=""/>
                </div>
            </@f.form>
        </#if>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
            <#list shows![] as show>
                <@showMacros.showCard show userShows isUserSpecificView/>
            </#list>
        </div>
    </@template.body>
</html>
