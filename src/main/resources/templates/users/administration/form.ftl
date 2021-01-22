<#import "/common/macros/base.ftl" as b/>
<#import "/common/macros/form.ftl" as f/>
<#import "/common/macros/card.ftl" as c/>

<#macro form user userTypes>
    <@f.form name="user" url="${springMacroRequestContext.contextPath}">
        <@c.card>
            <@c.body>
                <@b.row>
                    <@f.input label="Username" name="username" value=user.username/>
                    <@f.select label="Role" name="userType" options=userTypes value=user.userType/>

                    <@f.input label="Password" name="password" type="password"/>
                    <@f.input label="Paaword Repeat" name="passwordRepeat" type="password"/>

                    <@f.submit "Submit"/>
                </@b.row>
            </@c.body>
        </@c.card>
    </@f.form>

</#macro>