<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/error/template.ftl" as errorTemplate>

    <@template.head "404"/>
    <@template.body>
        <@errorTemplate.errorView header="error.404.header" message="error.404.message" statusCode=404/>
    </@template.body>
</html>