<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/administration/user/form.ftl" as form>

    <@template.head 'Episodes'/>
    <@template.body>
        <@b.flex>
            <@b.back_button showLabel=false/>
            <@b.h2 "admin.user.edit"/>
        </@b.flex>

        <@form.form user userRoles/>
    </@template.body>
</html>
