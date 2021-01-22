<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/macros/base.ftl" as b/>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/users/form.ftl" as form>

    <@template.head title/>
    <@template.body>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
            <#list shows as show>
                <@showMacros.showCard show userShows isUserSpecificView/>
            </#list>
        </div>
    </@template.body>
</html>
