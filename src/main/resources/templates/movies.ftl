<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/components/form.ftl" as f>
    <#import "/common/components/modal.ftl" as m>
    <#import "/common/macros/movie.ftl" as movieMacros/>

    <@template.head currentPage>
        <script src="<@s.url "/js/importer/search.js"/>"></script>
    </@template.head>
    <@template.body>
        <@b.row classes="mb-3">
            <@b.col classes="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                <h4><@s.messageArgs code="index.user.specific.number.of.movies" args=[movies?size, numberOfAllMovies]/></h4>
            </@b.col>
        </@b.row>

        <@b.hasPermission "ADMIN">
            <@b.row classes="mb-4">
                <@b.col>
                    <div class="dropdown">
                        <a class="btn btn-sm btn-primary float-end dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-add"></i>
                            <@b.localize "button.add"/>
                        </a>

                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="<@s.url "/movie/create"/>"><i class="fas fa-pencil me-2"></i><@b.localize "button.add.manually"/></a></li>
                            <li><a class="dropdown-item" data-bs-toggle="modal" data-bs-target="#importApiIdentifierModal"><i class="fas fa-wand-magic-sparkles me-2"></i><@b.localize "button.add.importer"/></a></li>
                        </ul>
                    </div>

                    <@m.modal id="importApiIdentifierModal" modalSize="modal-lg">
                        <@m.header "movie.api.create"/>
                        <@m.body>
                            <#assign objectName="importApiIdentifier"/>
                              <@b.row>
                                  <@f.select objectName=objectName label="show.apiIdentifiers.type" name="apiIdentifierType" options=apiConfigurationTypes value=apiConfigurationTypes[0]/>
                                  <@f.input objectName=objectName label="movie.apiIdentifiers.search" name="searchShowName" value="" url="movie/searchApi"/>
                              </@b.row>

                            <div id="searchResultContainer" data-form-url="/movie/createFromApi"></div>
                        </@m.body>
                        <@m.footer>
                            <@m.cancelButton/>
                        </@m.footer>
                    </@m.modal>
                </@b.col>
            </@b.row>
        </@b.hasPermission>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
            <#list movies![] as movie>
                <@movieMacros.movieCard movie/>
            </#list>
        </div>
    </@template.body>
</html>
