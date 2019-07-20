'use strict';

var count = 0;

function Request(valuename)   
{
    var rtnval;
    var nowAddress = unescape(location.href);
    var parameters = new Array();
    parameters = (nowAddress.slice(nowAddress.indexOf("?")+1,nowAddress.length)).split("&");
    for(var i = 0 ; i < parameters.length ; i++){
        if(parameters[i].indexOf(valuename) != -1){
            rtnval = parameters[i].split("=")[1];
            if(rtnval == undefined || rtnval == null){
                rtnval = "";
            }
            return rtnval;
        }
    }
}

var examples = {
  'web-tech': {
	list: Keyword,
    option: '{\n' +
            '  gridSize: 18,\n' +
            '  weightFactor: 3,\n' +
            '  fontFamily: \'Jeju Hallasan, serif\',\n' +
            '  color: function() {\n' +
            '    return ([\'#0000ff\', \'#009999\', \'#003366\', \'#6699cc\', \'#000066\'])[Math.floor(Math.random() * 5)]\n' +
            '  },\n' +
            '  click: function(item) { ' +
            '  		location.href("./index2.jsp?item="+escape(encodeURIComponent(item[0]))); '+
            '},\n'+
            '  hover: window.drawBox,\n' +
            '  backgroundColor: \'#ffffff\'\n' +
            '}',
    fontCSS: '//fonts.googleapis.com/earlyaccess/jejuhallasan.css',
    height: 470
  },
  
  'les-miz': {
    list: Keyword,
    option: '{\n' +
          '  gridSize: 18,\n' +
          '  weightFactor: 3,\n' +
          '  fontFamily: \'Average, Times, serif\',\n' +
          '  color: function() {\n' +
          '    return ([\'#d0d0d0\', \'#e11\', \'#44f\'])[Math.floor(Math.random() * 3)]\n' +
          '  },\n' +
          '  hover: window.drawBox,\n' +
          '  click: function(item) {\n' +
          '    alert(item[0] + \': \' + item[1]);\n' +
          '  },\n' +
          '  backgroundColor: \'#333\'\n' +
          '}',
    fontCSS: 'https://fonts.googleapis.com/css?family=Average',
    height: 470
  }
};

jQuery(function($) {
  var $form = $('#form');
  var $canvas = $('#canvas');
  var $htmlCanvas = $('#html-canvas');
  var $canvasContainer = $('#canvas-container');
  var $loading = $('#loading');
  var $list = $('#input-list');
  var $options = $('#config-option');
  var $width = $('#config-width');
  var $height = $('#config-height');
  var $dppx = $('#config-dppx');
  var $css = $('#config-css');
  var $webfontLink = $('#link-webfont');

  if (!WordCloud.isSupported) {
    $('#not-supported').prop('hidden', false);
    $form.find('textarea, input, select, button').prop('disabled', true);
    return;
  }
  
  var $box = $('<div id="box" hidden />');
  $canvasContainer.append($box);
  window.drawBox = function drawBox(item, dimension) {
    if (!dimension) {
      $box.prop('hidden', true);

      return;
    }

    var dppx = $dppx.val();

    $box.prop('hidden', false);
    $box.css({
      left: dimension.x / dppx + 'px',
      top: dimension.y / dppx + 'px',
      width: dimension.w / dppx + 'px',
      height: dimension.h / dppx + 'px'
    });
  };

  // Update the default value if we are running in a hdppx device
  if (('devicePixelRatio' in window) &&
      window.devicePixelRatio !== 1) {
    $dppx.val(window.devicePixelRatio);
  }

  $canvas.on('wordcloudstop', function wordcloudstopped(evt) {
    $loading.prop('hidden', true);
  });

  $form.on('submit', function formSubmit(evt) {
    evt.preventDefault();

    changeHash('');
  });

  $('#btn-save').on('click', function save(evt) {
    var url = $canvas[0].toDataURL();
    if ('download' in document.createElement('a')) {
      this.href = url;
    } else {
      evt.preventDefault();
      alert('Please right click and choose "Save As..." to save the generated image.');
      window.open(url, '_blank', 'width=500,height=300,menubar=yes');
    }
  });

  $('#btn-canvas').on('click', function showCanvas(evt) {
    $canvas.removeClass('hide');
    $htmlCanvas.addClass('hide');
    $('#btn-canvas').prop('disabled', true);
    $('#btn-html-canvas').prop('disabled', false);
  });

  $('#btn-html-canvas').on('click', function showCanvas(evt) {
    $canvas.addClass('hide');
    $htmlCanvas.removeClass('hide');
    $('#btn-canvas').prop('disabled', false);
    $('#btn-html-canvas').prop('disabled', true);
  });

  $('#btn-canvas').prop('disabled', true);
  $('#btn-html-canvas').prop('disabled', false);

  var $examples = $('#examples');
  $examples.on('change', function loadExample(evt) {
    changeHash(this.value);

    this.selectedIndex = 0;
    $examples.blur();
  });

  var run = function run() {
	//count++;
	
	//alert(count);
	  
    $loading.prop('hidden', false);

    // Load web font
    $webfontLink.prop('href', $css.val());

    // devicePixelRatio
    var devicePixelRatio = parseFloat($dppx.val());
    
    // Set the width and height
    var width = $width.val() ? $width.val() : $('#canvas-container').width();
    var height = $height.val() ? $height.val() : Math.floor(width * 0.65);
    var pixelWidth = width;
    var pixelHeight = height;

    if (devicePixelRatio !== 1) {
      $canvas.css({'width': width + 'px', 'height': height + 'px'});

      pixelWidth *= devicePixelRatio;
      pixelHeight *= devicePixelRatio;
    } else {
      $canvas.css({'width': '', 'height': '' });
    }

    $canvas.attr('width', pixelWidth);
    $canvas.attr('height', pixelHeight);

    $htmlCanvas.css({'width': pixelWidth + 'px', 'height': pixelHeight + 'px'});

    // Set the options object
    var options = {};
    if ($options.val()) {
      options = (function evalOptions() {
        try {
          return eval('(' + $options.val() + ')');
        } catch (error) {
          alert('The following Javascript error occurred in the option definition; all option will be ignored: \n\n' +
            error.toString());
          return {};
        }
      })();
    }

    // Set devicePixelRatio options
    if (devicePixelRatio !== 1) {
      if (!('gridSize' in options)) {
        options.gridSize = 8;
      }
      options.gridSize *= devicePixelRatio;

      if (options.origin) {
        if (typeof options.origin[0] == 'number')
          options.origin[0] *= devicePixelRatio;
        if (typeof options.origin[1] == 'number')
          options.origin[1] *= devicePixelRatio;
      }

      if (!('weightFactor' in options)) {
        options.weightFactor = 1;
      }
      if (typeof options.weightFactor == 'function') {
        var origWeightFactor = options.weightFactor;
        options.weightFactor =
          function weightFactorDevicePixelRatioWrap() {
            return origWeightFactor.apply(this, arguments) * devicePixelRatio;
          };
      } else {
        options.weightFactor *= devicePixelRatio;
      }
    }

    
    
    // Put the word list into options
    if ($list.val()) {
      var list = [];
      $.each($list.val().split(','), function each(i, line) {
        if (!$.trim(line))
          return;

        var lineArr = line.split(' ');
        var count = parseFloat(lineArr.shift()) || 0;
        list.push([lineArr.join(' '), count]);
      });
      options.list = list;
    }

    // All set, call the WordCloud()
    // Order matters here because the HTML canvas might by
    // set to display: none.
    WordCloud([$canvas[0], $htmlCanvas[0]], options);
  };

  var loadExampleData = function loadExampleData(name, item) {
    var example = examples[name];

    $options.val(example.option || '');
    $list.val(example.list || '');
    $css.val(example.fontCSS || '');
    $width.val(example.width || '');
    $height.val(example.height || '');
  };

  var changeHash = function changeHash(name) {
    if (window.location.hash === '#' + name ||
        (!window.location.hash && !name)) {
      // We got a same hash, call hashChanged() directly
      hashChanged();
    } else {
      // Actually change the hash to invoke hashChanged()
      window.location.hash = '#' + name;
    }
  };

  var hashChanged = function hashChanged() {
    var name = window.location.hash.substr(1);
    if (!name) {
      // If there is no name, run as-is.
      run();
    } else if (name in examples) {
      // If the name matches one of the example, load it and run it
      loadExampleData(name);
      run();
    } else {
      // If the name doesn't match, reset it.
      window.location.replace('#');
    }
  }

  $(window).on('hashchange', hashChanged);

  if (!window.location.hash ||
    !(window.location.hash.substr(1) in examples)) {
    // If the initial hash doesn't match to any of the examples,
    // or it doesn't exist, reset it to #love
    window.location.replace('#web-tech');
  } else {
    hashChanged();
  }
});
