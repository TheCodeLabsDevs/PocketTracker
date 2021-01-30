<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/card.ftl" as c/>

<#macro form user userRoles>
    <@f.form name="user" url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@c.card>
            <@c.body>
                <@b.row>
                    <@f.input label="user.form.username" name="username" value=user.username/>
                    <@f.select label="user.form.role" name="userRole" options=userRoles value=user.userRole/>

                    <@f.input label="user.form.password" name="password" type="password"/>
                    <@f.input label="user.form.passwordRepeat" name="passwordRepeat" type="password"/>

                    <@f.submit "button.save"/>
                </@b.row>
            </@c.body>
        </@c.card>
    </@f.form>
</#macro>