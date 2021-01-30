<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/card.ftl" as c/>
    <#import "/common/components/form.ftl" as f/>
    <#import "/administration/user/form.ftl" as form>

    <@template.head 'Episodes'/>
    <@template.body>

        <@b.flex>
            <@b.back_button label="button.cancel"/>
            <@b.h2 "admin.user.delete" />
        </@b.flex>

        <@f.form name="delete" url=springMacroRequestContext.contextPath>
            <@c.card>
                <@c.header user.name/>
                <@c.body message="admin.user.delete.message">
                    <@f.submit label="button.delete"/>
                </@c.body>
            </@c.card>
        </@f.form>
    </@template.body>
</html>
