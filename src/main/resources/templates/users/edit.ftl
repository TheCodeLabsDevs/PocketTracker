<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/users/form.ftl" as form>

    <@template.head 'Episodes'/>
    <@template.body>
        <@b.row>
            <@b.col "col-8">
                <h2> <i class="fas fa-user"></i> <@b.localize "title.profile"/></h2>
            </@b.col>
        </@b.row>

        <@form.form user authentications isGitlabConnected/>
    </@template.body>
</html>
