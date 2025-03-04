/*!
 *
 * plugins/ranges.js
 * Litepicker v2.0.12 (https://github.com/wakirin/Litepicker)
 * Package: litepicker (https://www.npmjs.com/package/litepicker)
 * License: MIT (https://github.com/wakirin/Litepicker/blob/master/LICENCE.md)
 * Copyright 2019-2021 Rinat G.
 *
 * Hash: b9a648207aabe31b2912
 *
 */ !(function (e) {
  var n = {};
  function t(r) {
    if (n[r]) return n[r].exports;
    var a = (n[r] = { i: r, l: !1, exports: {} });
    return e[r].call(a.exports, a, a.exports, t), (a.l = !0), a.exports;
  }
  (t.m = e),
    (t.c = n),
    (t.d = function (e, n, r) {
      t.o(e, n) || Object.defineProperty(e, n, { enumerable: !0, get: r });
    }),
    (t.r = function (e) {
      'undefined' != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, { value: 'Module' }),
        Object.defineProperty(e, '__esModule', { value: !0 });
    }),
    (t.t = function (e, n) {
      if ((1 & n && (e = t(e)), 8 & n)) return e;
      if (4 & n && 'object' == typeof e && e && e.__esModule) return e;
      var r = Object.create(null);
      if ((t.r(r), Object.defineProperty(r, 'default', { enumerable: !0, value: e }), 2 & n && 'string' != typeof e))
        for (var a in e)
          t.d(
            r,
            a,
            function (n) {
              return e[n];
            }.bind(null, a)
          );
      return r;
    }),
    (t.n = function (e) {
      var n =
        e && e.__esModule
          ? function () {
              return e.default;
            }
          : function () {
              return e;
            };
      return t.d(n, 'a', n), n;
    }),
    (t.o = function (e, n) {
      return Object.prototype.hasOwnProperty.call(e, n);
    }),
    (t.p = ''),
    t((t.s = 8));
})([
  function (e, n, t) {
    'use strict';
    e.exports = function (e) {
      var n = [];
      return (
        (n.toString = function () {
          return this.map(function (n) {
            var t = (function (e, n) {
              var t = e[1] || '',
                r = e[3];
              if (!r) return t;
              if (n && 'function' == typeof btoa) {
                var a =
                    ((i = r),
                    (s = btoa(unescape(encodeURIComponent(JSON.stringify(i))))),
                    (c = 'sourceMappingURL=data:application/json;charset=utf-8;base64,'.concat(s)),
                    '/*# '.concat(c, ' */')),
                  o = r.sources.map(function (e) {
                    return '/*# sourceURL='.concat(r.sourceRoot || '').concat(e, ' */');
                  });
                return [t].concat(o).concat([a]).join('\n');
              }
              var i, s, c;
              return [t].join('\n');
            })(n, e);
            return n[2] ? '@media '.concat(n[2], ' {').concat(t, '}') : t;
          }).join('');
        }),
        (n.i = function (e, t, r) {
          'string' == typeof e && (e = [[null, e, '']]);
          var a = {};
          if (r)
            for (var o = 0; o < this.length; o++) {
              var i = this[o][0];
              null != i && (a[i] = !0);
            }
          for (var s = 0; s < e.length; s++) {
            var c = [].concat(e[s]);
            (r && a[c[0]]) || (t && (c[2] ? (c[2] = ''.concat(t, ' and ').concat(c[2])) : (c[2] = t)), n.push(c));
          }
        }),
        n
      );
    };
  },
  function (e, n, t) {
    'use strict';
    var r,
      a = {},
      o = function () {
        return void 0 === r && (r = Boolean(window && document && document.all && !window.atob)), r;
      },
      i = (function () {
        var e = {};
        return function (n) {
          if (void 0 === e[n]) {
            var t = document.querySelector(n);
            if (window.HTMLIFrameElement && t instanceof window.HTMLIFrameElement)
              try {
                t = t.contentDocument.head;
              } catch (e) {
                t = null;
              }
            e[n] = t;
          }
          return e[n];
        };
      })();
    function s(e, n) {
      for (var t = [], r = {}, a = 0; a < e.length; a++) {
        var o = e[a],
          i = n.base ? o[0] + n.base : o[0],
          s = { css: o[1], media: o[2], sourceMap: o[3] };
        r[i] ? r[i].parts.push(s) : t.push((r[i] = { id: i, parts: [s] }));
      }
      return t;
    }
    function c(e, n) {
      for (var t = 0; t < e.length; t++) {
        var r = e[t],
          o = a[r.id],
          i = 0;
        if (o) {
          for (o.refs++; i < o.parts.length; i++) o.parts[i](r.parts[i]);
          for (; i < r.parts.length; i++) o.parts.push(m(r.parts[i], n));
        } else {
          for (var s = []; i < r.parts.length; i++) s.push(m(r.parts[i], n));
          a[r.id] = { id: r.id, refs: 1, parts: s };
        }
      }
    }
    function l(e) {
      var n = document.createElement('style');
      if (void 0 === e.attributes.nonce) {
        var r = t.nc;
        r && (e.attributes.nonce = r);
      }
      if (
        (Object.keys(e.attributes).forEach(function (t) {
          n.setAttribute(t, e.attributes[t]);
        }),
        'function' == typeof e.insert)
      )
        e.insert(n);
      else {
        var a = i(e.insert || 'head');
        if (!a) throw new Error("Couldn't find a style target. This probably means that the value for the 'insert' parameter is invalid.");
        a.appendChild(n);
      }
      return n;
    }
    var u,
      d =
        ((u = []),
        function (e, n) {
          return (u[e] = n), u.filter(Boolean).join('\n');
        });
    function p(e, n, t, r) {
      var a = t ? '' : r.css;
      if (e.styleSheet) e.styleSheet.cssText = d(n, a);
      else {
        var o = document.createTextNode(a),
          i = e.childNodes;
        i[n] && e.removeChild(i[n]), i.length ? e.insertBefore(o, i[n]) : e.appendChild(o);
      }
    }
    function f(e, n, t) {
      var r = t.css,
        a = t.media,
        o = t.sourceMap;
      if (
        (a && e.setAttribute('media', a),
        o &&
          btoa &&
          (r += '\n/*# sourceMappingURL=data:application/json;base64,'.concat(
            btoa(unescape(encodeURIComponent(JSON.stringify(o)))),
            ' */'
          )),
        e.styleSheet)
      )
        e.styleSheet.cssText = r;
      else {
        for (; e.firstChild; ) e.removeChild(e.firstChild);
        e.appendChild(document.createTextNode(r));
      }
    }
    var g = null,
      b = 0;
    function m(e, n) {
      var t, r, a;
      if (n.singleton) {
        var o = b++;
        (t = g || (g = l(n))), (r = p.bind(null, t, o, !1)), (a = p.bind(null, t, o, !0));
      } else
        (t = l(n)),
          (r = f.bind(null, t, n)),
          (a = function () {
            !(function (e) {
              if (null === e.parentNode) return !1;
              e.parentNode.removeChild(e);
            })(t);
          });
      return (
        r(e),
        function (n) {
          if (n) {
            if (n.css === e.css && n.media === e.media && n.sourceMap === e.sourceMap) return;
            r((e = n));
          } else a();
        }
      );
    }
    e.exports = function (e, n) {
      ((n = n || {}).attributes = 'object' == typeof n.attributes ? n.attributes : {}),
        n.singleton || 'boolean' == typeof n.singleton || (n.singleton = o());
      var t = s(e, n);
      return (
        c(t, n),
        function (e) {
          for (var r = [], o = 0; o < t.length; o++) {
            var i = t[o],
              l = a[i.id];
            l && (l.refs--, r.push(l));
          }
          e && c(s(e, n), n);
          for (var u = 0; u < r.length; u++) {
            var d = r[u];
            if (0 === d.refs) {
              for (var p = 0; p < d.parts.length; p++) d.parts[p]();
              delete a[d.id];
            }
          }
        }
      );
    };
  },
  ,
  ,
  ,
  ,
  ,
  ,
  function (e, n, t) {
    'use strict';
    t.r(n);
    t(9);
    function r(e, n) {
      var t = Object.keys(e);
      if (Object.getOwnPropertySymbols) {
        var r = Object.getOwnPropertySymbols(e);
        n &&
          (r = r.filter(function (n) {
            return Object.getOwnPropertyDescriptor(e, n).enumerable;
          })),
          t.push.apply(t, r);
      }
      return t;
    }
    function a(e) {
      for (var n = 1; n < arguments.length; n++) {
        var t = null != arguments[n] ? arguments[n] : {};
        n % 2
          ? r(Object(t), !0).forEach(function (n) {
              o(e, n, t[n]);
            })
          : Object.getOwnPropertyDescriptors
          ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(t))
          : r(Object(t)).forEach(function (n) {
              Object.defineProperty(e, n, Object.getOwnPropertyDescriptor(t, n));
            });
      }
      return e;
    }
    function o(e, n, t) {
      return n in e ? Object.defineProperty(e, n, { value: t, enumerable: !0, configurable: !0, writable: !0 }) : (e[n] = t), e;
    }
    Litepicker.add('ranges', {
      init: function (e) {
        var n = {
          position: 'left',
          customRanges: {},
          customRangesLabels: ['Today', 'Yesterday', 'Last 7 Days', 'Last 30 Days', 'This Month', 'Last Month'],
          force: !1,
          autoApply: e.options.autoApply,
        };
        (e.options.ranges = a(a({}, n), e.options.ranges)), (e.options.singleMode = !1);
        var t = e.options.ranges;
        if (!Object.keys(t.customRanges).length) {
          var r,
            i = e.DateTime();
          t.customRanges =
            (o((r = {}), t.customRangesLabels[0], [i.clone(), i.clone()]),
            o(r, t.customRangesLabels[1], [i.clone().subtract(1, 'day'), i.clone().subtract(1, 'day')]),
            o(r, t.customRangesLabels[2], [i.clone().subtract(6, 'day'), i]),
            o(r, t.customRangesLabels[3], [i.clone().subtract(29, 'day'), i]),
            o(
              r,
              t.customRangesLabels[4],
              (function (e) {
                var n = e.clone();
                return n.setDate(1), [n, new Date(e.getFullYear(), e.getMonth() + 1, 0)];
              })(i)
            ),
            o(
              r,
              t.customRangesLabels[5],
              (function (e) {
                var n = e.clone();
                return n.setDate(1), n.setMonth(e.getMonth() - 1), [n, new Date(e.getFullYear(), e.getMonth(), 0)];
              })(i)
            ),
            r);
        }
        e.on('render', function (n) {
          var r = document.createElement('div');
          (r.className = 'container__predefined-ranges'),
            (e.ui.dataset.rangesPosition = t.position),
            Object.keys(t.customRanges).forEach(function (a) {
              var o = t.customRanges[a],
                i = document.createElement('button');
              (i.innerText = a),
                (i.tabIndex = n.dataset.plugins.indexOf('keyboardnav') >= 0 ? 1 : -1),
                (i.dataset.start = o[0].getTime()),
                (i.dataset.end = o[1].getTime()),
                i.addEventListener('click', function (n) {
                  var r = n.target;
                  if (r) {
                    var a = e.DateTime(Number(r.dataset.start)),
                      o = e.DateTime(Number(r.dataset.end));
                    t.autoApply
                      ? (e.setDateRange(a, o, t.force), e.emit('ranges.selected', a, o), e.hide())
                      : ((e.datePicked = [a, o]), e.emit('ranges.preselect', a, o)),
                      (!e.options.inlineMode && t.autoApply) || e.gotoDate(a);
                  }
                }),
                r.appendChild(i);
            }),
            n.querySelector('.container__main').prepend(r);
        });
      },
    });
  },
  function (e, n, t) {
    var r = t(10);
    'string' == typeof r && (r = [[e.i, r, '']]);
    var a = {
      insert: function (e) {
        var n = document.querySelector('head'),
          t = window._lastElementInsertedByStyleLoader;
        window.disableLitepickerStyles ||
          (t ? (t.nextSibling ? n.insertBefore(e, t.nextSibling) : n.appendChild(e)) : n.insertBefore(e, n.firstChild),
          (window._lastElementInsertedByStyleLoader = e));
      },
      singleton: !1,
    };
    t(1)(r, a);
    r.locals && (e.exports = r.locals);
  },
  function (e, n, t) {
    (n = t(0)(!1)).push([
      e.i,
      '.litepicker[data-plugins*="ranges"] > .container__main > .container__predefined-ranges {\n  display: flex;\n  flex-direction: column;\n  align-items: flex-start;\n  background: var(--litepicker-container-months-color-bg);\n  box-shadow: -2px 0px 5px var(--litepicker-footer-box-shadow-color);\n  border-radius: 3px;\n}\n.litepicker[data-plugins*="ranges"][data-ranges-position="left"] > .container__main {\n  /* */\n}\n.litepicker[data-plugins*="ranges"][data-ranges-position="right"] > .container__main{\n  flex-direction: row-reverse;\n}\n.litepicker[data-plugins*="ranges"][data-ranges-position="right"] > .container__main > .container__predefined-ranges {\n  box-shadow: 2px 0px 2px var(--litepicker-footer-box-shadow-color);\n}\n.litepicker[data-plugins*="ranges"][data-ranges-position="top"] > .container__main {\n  flex-direction: column;\n}\n.litepicker[data-plugins*="ranges"][data-ranges-position="top"] > .container__main > .container__predefined-ranges {\n  flex-direction: row;\n  box-shadow: 2px 0px 2px var(--litepicker-footer-box-shadow-color);\n}\n.litepicker[data-plugins*="ranges"][data-ranges-position="bottom"] > .container__main {\n  flex-direction: column-reverse;\n}\n.litepicker[data-plugins*="ranges"][data-ranges-position="bottom"] > .container__main > .container__predefined-ranges {\n  flex-direction: row;\n  box-shadow: 2px 0px 2px var(--litepicker-footer-box-shadow-color);\n}\n.litepicker[data-plugins*="ranges"] > .container__main > .container__predefined-ranges button {\n  padding: 5px;\n  margin: 2px 0;\n}\n.litepicker[data-plugins*="ranges"][data-ranges-position="left"] > .container__main > .container__predefined-ranges button,\n.litepicker[data-plugins*="ranges"][data-ranges-position="right"] > .container__main > .container__predefined-ranges button{\n  width: 100%;\n  text-align: left;\n}\n.litepicker[data-plugins*="ranges"] > .container__main > .container__predefined-ranges button:hover {\n  cursor: default;\n  opacity: .6;\n}',
      '',
    ]),
      (e.exports = n);
  },
]);
