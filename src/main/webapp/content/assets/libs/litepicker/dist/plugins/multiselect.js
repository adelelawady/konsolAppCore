/*!
 *
 * plugins/multiselect.js
 * Litepicker v2.0.12 (https://github.com/wakirin/Litepicker)
 * Package: litepicker (https://www.npmjs.com/package/litepicker)
 * License: MIT (https://github.com/wakirin/Litepicker/blob/master/LICENCE.md)
 * Copyright 2019-2021 Rinat G.
 *
 * Hash: b9a648207aabe31b2912
 *
 */ !(function (e) {
  var t = {};
  function n(r) {
    if (t[r]) return t[r].exports;
    var i = (t[r] = { i: r, l: !1, exports: {} });
    return e[r].call(i.exports, i, i.exports, n), (i.l = !0), i.exports;
  }
  (n.m = e),
    (n.c = t),
    (n.d = function (e, t, r) {
      n.o(e, t) || Object.defineProperty(e, t, { enumerable: !0, get: r });
    }),
    (n.r = function (e) {
      'undefined' != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, { value: 'Module' }),
        Object.defineProperty(e, '__esModule', { value: !0 });
    }),
    (n.t = function (e, t) {
      if ((1 & t && (e = n(e)), 8 & t)) return e;
      if (4 & t && 'object' == typeof e && e && e.__esModule) return e;
      var r = Object.create(null);
      if ((n.r(r), Object.defineProperty(r, 'default', { enumerable: !0, value: e }), 2 & t && 'string' != typeof e))
        for (var i in e)
          n.d(
            r,
            i,
            function (t) {
              return e[t];
            }.bind(null, i)
          );
      return r;
    }),
    (n.n = function (e) {
      var t =
        e && e.__esModule
          ? function () {
              return e.default;
            }
          : function () {
              return e;
            };
      return n.d(t, 'a', t), t;
    }),
    (n.o = function (e, t) {
      return Object.prototype.hasOwnProperty.call(e, t);
    }),
    (n.p = ''),
    n((n.s = 11));
})([
  function (e, t, n) {
    'use strict';
    e.exports = function (e) {
      var t = [];
      return (
        (t.toString = function () {
          return this.map(function (t) {
            var n = (function (e, t) {
              var n = e[1] || '',
                r = e[3];
              if (!r) return n;
              if (t && 'function' == typeof btoa) {
                var i =
                    ((l = r),
                    (a = btoa(unescape(encodeURIComponent(JSON.stringify(l))))),
                    (c = 'sourceMappingURL=data:application/json;charset=utf-8;base64,'.concat(a)),
                    '/*# '.concat(c, ' */')),
                  o = r.sources.map(function (e) {
                    return '/*# sourceURL='.concat(r.sourceRoot || '').concat(e, ' */');
                  });
                return [n].concat(o).concat([i]).join('\n');
              }
              var l, a, c;
              return [n].join('\n');
            })(t, e);
            return t[2] ? '@media '.concat(t[2], ' {').concat(n, '}') : n;
          }).join('');
        }),
        (t.i = function (e, n, r) {
          'string' == typeof e && (e = [[null, e, '']]);
          var i = {};
          if (r)
            for (var o = 0; o < this.length; o++) {
              var l = this[o][0];
              null != l && (i[l] = !0);
            }
          for (var a = 0; a < e.length; a++) {
            var c = [].concat(e[a]);
            (r && i[c[0]]) || (n && (c[2] ? (c[2] = ''.concat(n, ' and ').concat(c[2])) : (c[2] = n)), t.push(c));
          }
        }),
        t
      );
    };
  },
  function (e, t, n) {
    'use strict';
    var r,
      i = {},
      o = function () {
        return void 0 === r && (r = Boolean(window && document && document.all && !window.atob)), r;
      },
      l = (function () {
        var e = {};
        return function (t) {
          if (void 0 === e[t]) {
            var n = document.querySelector(t);
            if (window.HTMLIFrameElement && n instanceof window.HTMLIFrameElement)
              try {
                n = n.contentDocument.head;
              } catch (e) {
                n = null;
              }
            e[t] = n;
          }
          return e[t];
        };
      })();
    function a(e, t) {
      for (var n = [], r = {}, i = 0; i < e.length; i++) {
        var o = e[i],
          l = t.base ? o[0] + t.base : o[0],
          a = { css: o[1], media: o[2], sourceMap: o[3] };
        r[l] ? r[l].parts.push(a) : n.push((r[l] = { id: l, parts: [a] }));
      }
      return n;
    }
    function c(e, t) {
      for (var n = 0; n < e.length; n++) {
        var r = e[n],
          o = i[r.id],
          l = 0;
        if (o) {
          for (o.refs++; l < o.parts.length; l++) o.parts[l](r.parts[l]);
          for (; l < r.parts.length; l++) o.parts.push(v(r.parts[l], t));
        } else {
          for (var a = []; l < r.parts.length; l++) a.push(v(r.parts[l], t));
          i[r.id] = { id: r.id, refs: 1, parts: a };
        }
      }
    }
    function s(e) {
      var t = document.createElement('style');
      if (void 0 === e.attributes.nonce) {
        var r = n.nc;
        r && (e.attributes.nonce = r);
      }
      if (
        (Object.keys(e.attributes).forEach(function (n) {
          t.setAttribute(n, e.attributes[n]);
        }),
        'function' == typeof e.insert)
      )
        e.insert(t);
      else {
        var i = l(e.insert || 'head');
        if (!i) throw new Error("Couldn't find a style target. This probably means that the value for the 'insert' parameter is invalid.");
        i.appendChild(t);
      }
      return t;
    }
    var u,
      p =
        ((u = []),
        function (e, t) {
          return (u[e] = t), u.filter(Boolean).join('\n');
        });
    function f(e, t, n, r) {
      var i = n ? '' : r.css;
      if (e.styleSheet) e.styleSheet.cssText = p(t, i);
      else {
        var o = document.createTextNode(i),
          l = e.childNodes;
        l[t] && e.removeChild(l[t]), l.length ? e.insertBefore(o, l[t]) : e.appendChild(o);
      }
    }
    function d(e, t, n) {
      var r = n.css,
        i = n.media,
        o = n.sourceMap;
      if (
        (i && e.setAttribute('media', i),
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
    var m = null,
      b = 0;
    function v(e, t) {
      var n, r, i;
      if (t.singleton) {
        var o = b++;
        (n = m || (m = s(t))), (r = f.bind(null, n, o, !1)), (i = f.bind(null, n, o, !0));
      } else
        (n = s(t)),
          (r = d.bind(null, n, t)),
          (i = function () {
            !(function (e) {
              if (null === e.parentNode) return !1;
              e.parentNode.removeChild(e);
            })(n);
          });
      return (
        r(e),
        function (t) {
          if (t) {
            if (t.css === e.css && t.media === e.media && t.sourceMap === e.sourceMap) return;
            r((e = t));
          } else i();
        }
      );
    }
    e.exports = function (e, t) {
      ((t = t || {}).attributes = 'object' == typeof t.attributes ? t.attributes : {}),
        t.singleton || 'boolean' == typeof t.singleton || (t.singleton = o());
      var n = a(e, t);
      return (
        c(n, t),
        function (e) {
          for (var r = [], o = 0; o < n.length; o++) {
            var l = n[o],
              s = i[l.id];
            s && (s.refs--, r.push(s));
          }
          e && c(a(e, t), t);
          for (var u = 0; u < r.length; u++) {
            var p = r[u];
            if (0 === p.refs) {
              for (var f = 0; f < p.parts.length; f++) p.parts[f]();
              delete i[p.id];
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
  ,
  ,
  ,
  function (e, t, n) {
    'use strict';
    n.r(t);
    n(12);
    function r(e) {
      return (
        (function (e) {
          if (Array.isArray(e)) return i(e);
        })(e) ||
        (function (e) {
          if ('undefined' != typeof Symbol && Symbol.iterator in Object(e)) return Array.from(e);
        })(e) ||
        (function (e, t) {
          if (!e) return;
          if ('string' == typeof e) return i(e, t);
          var n = Object.prototype.toString.call(e).slice(8, -1);
          'Object' === n && e.constructor && (n = e.constructor.name);
          if ('Map' === n || 'Set' === n) return Array.from(e);
          if ('Arguments' === n || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return i(e, t);
        })(e) ||
        (function () {
          throw new TypeError(
            'Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.'
          );
        })()
      );
    }
    function i(e, t) {
      (null == t || t > e.length) && (t = e.length);
      for (var n = 0, r = new Array(t); n < t; n++) r[n] = e[n];
      return r;
    }
    function o(e, t) {
      var n = Object.keys(e);
      if (Object.getOwnPropertySymbols) {
        var r = Object.getOwnPropertySymbols(e);
        t &&
          (r = r.filter(function (t) {
            return Object.getOwnPropertyDescriptor(e, t).enumerable;
          })),
          n.push.apply(n, r);
      }
      return n;
    }
    function l(e) {
      for (var t = 1; t < arguments.length; t++) {
        var n = null != arguments[t] ? arguments[t] : {};
        t % 2
          ? o(Object(n), !0).forEach(function (t) {
              a(e, t, n[t]);
            })
          : Object.getOwnPropertyDescriptors
          ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(n))
          : o(Object(n)).forEach(function (t) {
              Object.defineProperty(e, t, Object.getOwnPropertyDescriptor(n, t));
            });
      }
      return e;
    }
    function a(e, t, n) {
      return t in e ? Object.defineProperty(e, t, { value: n, enumerable: !0, configurable: !0, writable: !0 }) : (e[t] = n), e;
    }
    Litepicker.add('multiselect', {
      init: function (e) {
        Object.defineProperties(e, {
          multipleDates: { value: [], enumerable: !0, writable: !0 },
          preMultipleDates: { value: [], writable: !0 },
        });
        (e.options.multiselect = l(l({}, { max: null }), e.options.multiselect)),
          (e.options.autoApply = e.options.inlineMode),
          (e.options.showTooltip = !1);
        var t = function () {
          var t = e.preMultipleDates.length,
            n = e.ui.querySelector('.preview-date-range');
          if (n && t > 0) {
            var r = e.pluralSelector(t),
              i = e.options.tooltipText[r] ? e.options.tooltipText[r] : '['.concat(r, ']'),
              o = ''.concat(t, ' ').concat(i);
            n.innerText = o;
          }
        };
        e.on('before:show', function () {
          e.preMultipleDates = r(e.multipleDates);
        }),
          e.on('show', function () {
            t();
          }),
          e.on('before:click', function (n) {
            if (n.classList.contains('day-item')) {
              if (((e.preventClick = !0), n.classList.contains('is-locked'))) return void n.blur();
              var r = Number(n.dataset.time);
              n.classList.contains('is-selected')
                ? ((e.preMultipleDates = e.preMultipleDates.filter(function (e) {
                    return e !== r;
                  })),
                  e.emit('multiselect.deselect', e.DateTime(r)))
                : ((e.preMultipleDates[e.preMultipleDates.length] = r), e.emit('multiselect.select', e.DateTime(r))),
                e.options.autoApply && e.emit('button:apply'),
                e.render(),
                t();
            }
          }),
          e.on('render:day', function (t) {
            var n = e.preMultipleDates.filter(function (e) {
                return e === Number(t.dataset.time);
              }).length,
              r = Number(e.options.multiselect.max);
            n ? t.classList.add('is-selected') : r && e.preMultipleDates.length >= r && t.classList.add('is-locked');
          }),
          e.on('button:cancel', function () {
            e.preMultipleDates.length = 0;
          }),
          e.on('button:apply', function () {
            e.multipleDates = r(e.preMultipleDates).sort(function (e, t) {
              return e - t;
            });
          }),
          e.on('clear:selection', function () {
            e.clearMultipleDates(), e.render();
          }),
          (e.clearMultipleDates = function () {
            (e.preMultipleDates.length = 0), (e.multipleDates.length = 0);
          }),
          (e.getMultipleDates = function () {
            return e.multipleDates.map(function (t) {
              return e.DateTime(t);
            });
          }),
          (e.multipleDatesToString = function () {
            var t = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : 'YYYY-MM-DD',
              n = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : ',';
            return e.multipleDates
              .map(function (n) {
                return e.DateTime(n).format(t);
              })
              .join(n);
          });
      },
    });
  },
  function (e, t, n) {
    var r = n(13);
    'string' == typeof r && (r = [[e.i, r, '']]);
    var i = {
      insert: function (e) {
        var t = document.querySelector('head'),
          n = window._lastElementInsertedByStyleLoader;
        window.disableLitepickerStyles ||
          (n ? (n.nextSibling ? t.insertBefore(e, n.nextSibling) : t.appendChild(e)) : t.insertBefore(e, t.firstChild),
          (window._lastElementInsertedByStyleLoader = e));
      },
      singleton: !1,
    };
    n(1)(r, i);
    r.locals && (e.exports = r.locals);
  },
  function (e, t, n) {
    (t = n(0)(!1)).push([
      e.i,
      ':root {\n  --litepicker-multiselect-is-selected-color-bg: #2196f3;\n  --litepicker-multiselect-is-selected-color: #fff;\n  --litepicker-multiselect-hover-color-bg: #2196f3;\n  --litepicker-multiselect-hover-color: #fff;\n}\n\n.litepicker[data-plugins*="multiselect"] .container__days .day-item {\n  position: relative;\n  z-index: 1;\n}\n\n.litepicker[data-plugins*="multiselect"] .container__days .day-item:not(.is-locked):after {\n  content: \'\';\n  position: absolute;\n  width: 27px;\n  height: 27px;\n  top: 50%;\n  left: 50%;\n  z-index: -1;\n  border-radius: 50%;\n  transform: translate(-50%, -50%);\n}\n\n.litepicker[data-plugins*="multiselect"] .container__days .day-item:not(.is-locked):hover {\n  box-shadow: none;\n  color: var(--litepicker-day-color);\n  font-weight: bold;\n}\n\n\n.litepicker[data-plugins*="multiselect"] .container__days .day-item.is-selected,\n.litepicker[data-plugins*="multiselect"] .container__days .day-item.is-selected:hover {\n  color: var(--litepicker-multiselect-is-selected-color);\n}\n\n.litepicker[data-plugins*="multiselect"] .container__days .day-item.is-selected:after {\n  color: var(--litepicker-multiselect-is-selected-color);\n  background-color: var(--litepicker-multiselect-is-selected-color-bg);\n}\n',
      '',
    ]),
      (e.exports = t);
  },
]);
