<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/error/template.ftl" as errorTemplate>

    <@template.head "500"/>
    <@template.body>
        <@errorTemplate.errorView header="error.500.header" message="error.500.message" statusCode=500/>
    </@template.body>
</html>