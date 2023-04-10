<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/error/template.ftl" as errorTemplate>

    <@template.head "401"/>
    <@template.body>
        <@errorTemplate.errorView header="error.401.header" message="error.401.message"/>
    </@template.body>
</html>