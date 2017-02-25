$.get("listfile2?fid=0",function(Data)
{
	console.log(Data);
	var demoData = JSON.parse(Data);
				var container = document.getElementById('bind-container');

        // Create list view model
		var listViewModel = new ListViewModel();
		
	    // Add folders to list
	    console.log(demoData);
	    console.log(demoData.Folders);
		if (demoData.Folders) {
		    for (var j = 0; j < demoData.Folders.length; j++) {
		        var folder = demoData.Folders[j],
                    folderViewModel = new ItemViewModel(listViewModel, true, folder.Id, folder.Title, folder.ParentId);

		        listViewModel.allItems.push(folderViewModel);
		    }
		}

	    // Add files to list
		if (demoData.Files) {
		    for (var k = 0; k < demoData.Files.length; k++) {
		        var file = demoData.Files[k],
                    fileViewModel = new ItemViewModel(listViewModel, false, file.Id, file.Title, file.FolderId);

		        listViewModel.allItems.push(fileViewModel);
		    }
		}

	    // Set grid options
		if (demoData.GridOptions) {
		    listViewModel.searchQuery(demoData.GridOptions.SearchQuery);
		    listViewModel.sortColumn(demoData.GridOptions.Column);
		    listViewModel.sortDirection(demoData.GridOptions.Direction);
		}

	    // Initialize list view model
		listViewModel.initialize();

		// Bind view model to view
		ko.applyBindings(listViewModel, container);
	
	}).done(function() {
    console.log( "second success" );
  })
  .fail(function() {
    console.log( "error" );
  })
  .always(function() {
    console.log( "complete" );
  });