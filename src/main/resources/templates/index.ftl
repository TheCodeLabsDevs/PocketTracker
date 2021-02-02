<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/users/form.ftl" as form>

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

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
            <#list shows![] as show>
                <@showMacros.showCard show userShows isUserSpecificView/>
            </#list>
        </div>
    </@template.body>
</html>
