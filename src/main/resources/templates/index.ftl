<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/form.ftl" as f>
    <#import "/common/macros/show.ftl" as showMacros/>

    <@template.head currentPage/>
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
                        <@b.button label="button.add" url="/show/create" style="btn-sm btn-primary float-end"/>
                    </@b.col>
                </@b.row>
            </@b.hasPermission>
        </#if>

        <#if isUserSpecificView>
            <@f.form name="sortOptionForm" url=springMacroRequestContext.getRequestUri() rawUrl=true>

                <div class="d-flex justify-content-end">
                    <a class="btn <#if showDislikedShows>btn-secondary<#else>btn-outline-secondary</#if> mb-4 me-2" role="button" id="showDislikedShows">
                        <#if showDislikedShows>
                            <i class="fas fa-thumbs-down"></i>
                        <#else>
                            <i class="far fa-thumbs-down"></i>
                        </#if>
                    </a>
                    <input type="hidden" name="showDislikedShows" value="${showDislikedShows?c}">

                    <@f.select name="filterOption" options=showFilterOptions value=currentFilterOption.name() size="me-2"/>
                    <@f.select name="sortOption" options=showSortOptions value=currentSortOption.name() size=""/>
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
