<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/card.ftl" as c/>
    <#import "/common/components/form.ftl" as f/>
    <#import "/common/components/table.ftl" as t/>

    <@template.head 'Episodes'/>
    <@template.body>
        <@b.h2 title=show.name raw=true/>

        <@c.card>
            <@c.body>
                <@f.form name="show" url=springMacroRequestContext.getRequestUri() rawUrl=true>
                    <@b.row>
                        <@f.input label="show.name" name="name" value=show.name!""/>
                        <#assign today = .now?string['yyyy-MM-dd']/>
                        <@f.input label="show.firstAired" name="firstAired" value=show.getFirstAiredReadable()!today/>

                        <@f.textarea label="show.description" name="description" value=show.description!"" size="col-12"/>

                        <@f.select label="show.type" name="type" options=showTypes value=show.type.name()/>

                        <@b.col classes="mt-4">
                            <@b.button label="button.cancel" url="/show/${show.id}" style="btn-secondary"/>
                            <@f.submit col=false classes="float-end"/>
                        </@b.col>
                    </@b.row>
                </@f.form>
            </@c.body>
        </@c.card>
    </@template.body>
</html>
