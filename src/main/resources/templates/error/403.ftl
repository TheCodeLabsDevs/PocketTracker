<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/error/template.ftl" as errorTemplate>

    <@template.head "403"/>
    <@template.body>
        <@errorTemplate.errorView header="error.403.header" message="error.403.message" statusCode=403/>
    </@template.body>
</html>