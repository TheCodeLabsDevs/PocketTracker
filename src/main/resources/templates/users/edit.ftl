<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/macros/base.ftl" as b/>
    <#import "/users/form.ftl" as form>

    <@template.head 'Episodes'/>
    <@template.body>
        <@b.row>
            <@b.col "col-8">
                <@b.h2 "Profil bearbeiten"/>
            </@b.col>
        </@b.row>

        <@form.form user/>
    </@template.body>
</html>
