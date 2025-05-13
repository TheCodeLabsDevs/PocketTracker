<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/form.ftl" as f>
    <#import "/common/components/modal.ftl" as m>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/common/macros/movie.ftl" as movieMacros/>

    <@template.head currentPage/>
    <@template.body>
        <@b.row classes="mb-3">
            <@b.col classes="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                <h4><@s.messageArgs code="title.search" args=[shows?size + movies?size]/></h4>
            </@b.col>
        </@b.row>

        <@b.row classes="mb-3">
            <@b.col classes="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                <h4><@s.messageArgs code="title.search.shows" args=[shows?size]/></h4>
            </@b.col>
        </@b.row>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
            <#list shows![] as show>
                <@showMacros.showCard show/>
            </#list>
        </div>

        <@b.row classes="mt-5 mb-3">
            <@b.col classes="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                <h4><@s.messageArgs code="title.search.movies" args=[movies?size]/></h4>
            </@b.col>
        </@b.row>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
            <#list movies![] as movie>
                <@movieMacros.movieCard movie/>
            </#list>
        </div>
    </@template.body>
</html>
