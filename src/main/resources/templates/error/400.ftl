<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/error/template.ftl" as errorTemplate>

    <@template.head "400"/>
    <@template.body>
        <@errorTemplate.errorView header="error.400.header" message="error.400.message"/>
    </@template.body>
</html>