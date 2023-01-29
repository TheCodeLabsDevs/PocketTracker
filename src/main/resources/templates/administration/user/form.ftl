<#import "/common/components/base.ftl" as b/>
<#import "/common/components/form.ftl" as f/>
<#import "/common/components/card.ftl" as c/>

<#macro form user userRoles>
    <#assign objectName="user"/>
    <@f.form name=objectName url=springMacroRequestContext.getRequestUri() rawUrl=true>
        <@c.card>
            <@c.body>
                <@b.row>
                    <@f.input objectName=objectName label="user.form.username" name="username" value=user.username/>
                    <@f.select objectName=objectName label="user.form.role" name="userRole" options=userRoles value=user.userRole/>

                    <@f.input objectName=objectName label="user.form.password" name="password" type="password"/>
                    <@f.input objectName=objectName label="user.form.passwordRepeat" name="passwordRepeat" type="password"/>

                    <@f.submit "button.save"/>
                </@b.row>
            </@c.body>
        </@c.card>
    </@f.form>
</#macro>