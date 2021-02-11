<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/form.ftl" as f>
    <#import "/common/macros/show.ftl" as showMacros/>

    <@template.head currentPage/>
    <@template.body>
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
            <@b.row>
                <@f.form name="sortOptionForm" url=springMacroRequestContext.getRequestUri() method="GET" rawUrl=true csrf=false>
                    <@f.select name="sortOption" options=showSortOptions value=currentSortOption.name() size="col-12 col-md-6 col-lg-3" classes="float-end"/>
                </@f.form>
            </@b.row>
        </#if>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
            <#list shows![] as show>
                <@showMacros.showCard show userShows isUserSpecificView/>
            </#list>
        </div>
    </@template.body>
</html>
