/*!
 *
 * plugins/mobilefriendly.js
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
    n((n.s = 5));
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
                    ((a = r),
                    (l = btoa(unescape(encodeURIComponent(JSON.stringify(a))))),
                    (c = 'sourceMappingURL=data:application/json;charset=utf-8;base64,'.concat(l)),
                    '/*# '.concat(c, ' */')),
                  o = r.sources.map(function (e) {
                    return '/*# sourceURL='.concat(r.sourceRoot || '').concat(e, ' */');
                  });
                return [n].concat(o).concat([i]).join('\n');
              }
              var a, l, c;
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
              var a = this[o][0];
              null != a && (i[a] = !0);
            }
          for (var l = 0; l < e.length; l++) {
            var c = [].concat(e[l]);
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
      a = (function () {
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
    function l(e, t) {
      for (var n = [], r = {}, i = 0; i < e.length; i++) {
        var o = e[i],
          a = t.base ? o[0] + t.base : o[0],
          l = { css: o[1], media: o[2], sourceMap: o[3] };
        r[a] ? r[a].parts.push(l) : n.push((r[a] = { id: a, parts: [l] }));
      }
      return n;
    }
    function c(e, t) {
      for (var n = 0; n < e.length; n++) {
        var r = e[n],
          o = i[r.id],
          a = 0;
        if (o) {
          for (o.refs++; a < o.parts.length; a++) o.parts[a](r.parts[a]);
          for (; a < r.parts.length; a++) o.parts.push(b(r.parts[a], t));
        } else {
          for (var l = []; a < r.parts.length; a++) l.push(b(r.parts[a], t));
          i[r.id] = { id: r.id, refs: 1, parts: l };
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
        var i = a(e.insert || 'head');
        if (!i) throw new Error("Couldn't find a style target. This probably means that the value for the 'insert' parameter is invalid.");
        i.appendChild(t);
      }
      return t;
    }
    var u,
      d =
        ((u = []),
        function (e, t) {
          return (u[e] = t), u.filter(Boolean).join('\n');
        });
    function p(e, t, n, r) {
      var i = n ? '' : r.css;
      if (e.styleSheet) e.styleSheet.cssText = d(t, i);
      else {
        var o = document.createTextNode(i),
          a = e.childNodes;
        a[t] && e.removeChild(a[t]), a.length ? e.insertBefore(o, a[t]) : e.appendChild(o);
      }
    }
    function f(e, t, n) {
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
      h = 0;
    function b(e, t) {
      var n, r, i;
      if (t.singleton) {
        var o = h++;
        (n = m || (m = s(t))), (r = p.bind(null, n, o, !1)), (i = p.bind(null, n, o, !0));
      } else
        (n = s(t)),
          (r = f.bind(null, n, t)),
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
      var n = l(e, t);
      return (
        c(n, t),
        function (e) {
          for (var r = [], o = 0; o < n.length; o++) {
            var a = n[o],
              s = i[a.id];
            s && (s.refs--, r.push(s));
          }
          e && c(l(e, t), t);
          for (var u = 0; u < r.length; u++) {
            var d = r[u];
            if (0 === d.refs) {
              for (var p = 0; p < d.parts.length; p++) d.parts[p]();
              delete i[d.id];
            }
          }
        }
      );
    };
  },
  ,
  ,
  ,
  function (e, t, n) {
    'use strict';
    n.r(t);
    n(6);
    function r(e, t) {
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
    function i(e) {
      for (var t = 1; t < arguments.length; t++) {
        var n = null != arguments[t] ? arguments[t] : {};
        t % 2
          ? r(Object(n), !0).forEach(function (t) {
              o(e, t, n[t]);
            })
          : Object.getOwnPropertyDescriptors
          ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(n))
          : r(Object(n)).forEach(function (t) {
              Object.defineProperty(e, t, Object.getOwnPropertyDescriptor(n, t));
            });
      }
      return e;
    }
    function o(e, t, n) {
      return t in e ? Object.defineProperty(e, t, { value: n, enumerable: !0, configurable: !0, writable: !0 }) : (e[t] = n), e;
    }
    Litepicker.add('mobilefriendly', {
      init: function (e) {
        var t = e.options;
        (e.options.mobilefriendly = i(i({}, { breakpoint: 480 }), t.mobilefriendly)),
          Object.defineProperties(e, {
            xTouchDown: { value: null, writable: !0 },
            yTouchDown: { value: null, writable: !0 },
            touchTargetMonth: { value: null, writable: !0 },
          });
        var n = !1;
        try {
          var r = Object.defineProperty({}, 'passive', {
            get: function () {
              n = !0;
            },
          });
          window.addEventListener('testPassive', null, r), window.removeEventListener('testPassive', null, r);
        } catch (e) {}
        function o() {
          var t = 'portrait' === a();
          return window.matchMedia('(max-device-'.concat(t ? 'width' : 'height', ': ').concat(e.options.mobilefriendly.breakpoint, 'px)'))
            .matches;
        }
        function a() {
          return 'orientation' in window.screen && 'type' in window.screen.orientation
            ? window.screen.orientation.type.replace(/\-\w+$/, '')
            : window.matchMedia('(orientation: portrait)').matches
            ? 'portrait'
            : 'landscape';
        }
        function l() {
          'portrait' === a()
            ? ((e.options.numberOfMonths = 1), (e.options.numberOfColumns = 1))
            : ((e.options.numberOfMonths = 2), (e.options.numberOfColumns = 2));
        }
        var c = function (t) {
            var n = t.touches[0];
            (e.xTouchDown = n.clientX), (e.yTouchDown = n.clientY);
          },
          s = function (t) {
            if (e.xTouchDown && e.yTouchDown) {
              var n = t.touches[0].clientX,
                r = t.touches[0].clientY,
                i = e.xTouchDown - n,
                o = e.yTouchDown - r,
                a = Math.abs(i) > Math.abs(o),
                l = e.options.numberOfMonths,
                c = null,
                s = !1,
                u = '',
                d = Array.from(e.ui.querySelectorAll('.month-item'));
              if (a) {
                var p = e.DateTime(e.ui.querySelector('.day-item').dataset.time),
                  f = Number(''.concat(1 - Math.abs(i) / 100)),
                  m = 0;
                if (i > 0) {
                  (m = -Math.abs(i)), (c = p.clone().add(l, 'month'));
                  var h = e.options.maxDate;
                  (s = !h || c.isSameOrBefore(e.DateTime(h), 'month')), (u = 'next');
                } else {
                  (m = Math.abs(i)), (c = p.clone().subtract(l, 'month'));
                  var b = e.options.minDate;
                  (s = !b || c.isSameOrAfter(e.DateTime(b), 'month')), (u = 'prev');
                }
                s &&
                  d.map(function (e) {
                    (e.style.opacity = f), (e.style.transform = 'translateX('.concat(m, 'px)'));
                  });
              }
              Math.abs(i) + Math.abs(o) > 100 && a && c && s && ((e.touchTargetMonth = u), e.gotoDate(c));
            }
          },
          u = function (t) {
            e.touchTargetMonth ||
              Array.from(e.ui.querySelectorAll('.month-item')).map(function (e) {
                (e.style.transform = 'translateX(0px)'), (e.style.opacity = 1);
              });
            (e.xTouchDown = null), (e.yTouchDown = null);
          };
        (e.backdrop = document.createElement('div')),
          (e.backdrop.className = 'litepicker-backdrop'),
          e.backdrop.addEventListener('click', e.hide()),
          t.element && t.element.parentNode && t.element.parentNode.appendChild(e.backdrop),
          window.addEventListener('orientationchange', function (n) {
            window.addEventListener('resize', function n() {
              if (o() && e.isShowning()) {
                var r = a();
                switch (r) {
                  case 'landscape':
                    (t.numberOfMonths = 2), (t.numberOfColumns = 2);
                    break;
                  default:
                    (t.numberOfMonths = 1), (t.numberOfColumns = 1);
                }
                e.ui.classList.toggle('mobilefriendly-portrait', 'portrait' === r),
                  e.ui.classList.toggle('mobilefriendly-landscape', 'landscape' === r),
                  e.render();
              }
              window.removeEventListener('resize', n);
            });
          }),
          t.inlineMode && o() && (window.dispatchEvent(new Event('orientationchange')), window.dispatchEvent(new Event('resize'))),
          e.on('before:show', function (t) {
            if (((e.triggerElement = t), !e.options.inlineMode && o())) {
              e.emit('mobilefriendly.before:show', t),
                (e.ui.style.position = 'fixed'),
                (e.ui.style.display = 'block'),
                l(),
                e.scrollToDate(t),
                e.render();
              var n = a();
              e.ui.classList.add('mobilefriendly'),
                e.ui.classList.toggle('mobilefriendly-portrait', 'portrait' === n),
                e.ui.classList.toggle('mobilefriendly-landscape', 'landscape' === n),
                (e.ui.style.top = '50%'),
                (e.ui.style.left = '50%'),
                (e.ui.style.right = null),
                (e.ui.style.bottom = null),
                (e.ui.style.zIndex = e.options.zIndex),
                (e.backdrop.style.display = 'block'),
                (e.backdrop.style.zIndex = e.options.zIndex - 1),
                document.body.classList.add('litepicker-open'),
                (t || e.options.element).blur(),
                e.emit('mobilefriendly.show', t);
            } else o() && (l(), e.render());
          }),
          e.on('render', function (t) {
            e.touchTargetMonth &&
              Array.from(e.ui.querySelectorAll('.month-item')).map(function (t) {
                return t.classList.add('touch-target-'.concat(e.touchTargetMonth));
              });
            e.touchTargetMonth = null;
          }),
          e.on('hide', function () {
            document.body.classList.remove('litepicker-open'),
              (e.backdrop.style.display = 'none'),
              e.ui.classList.remove('mobilefriendly', 'mobilefriendly-portrait', 'mobilefriendly-landscape');
          }),
          e.on('destroy', function () {
            e.backdrop && e.backdrop.parentNode && e.backdrop.parentNode.removeChild(e.backdrop);
          }),
          e.ui.addEventListener('touchstart', c, !!n && { passive: !0 }),
          e.ui.addEventListener('touchmove', s, !!n && { passive: !0 }),
          e.ui.addEventListener('touchend', u, !!n && { passive: !0 });
      },
    });
  },
  function (e, t, n) {
    var r = n(7);
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
      ':root {\n  --litepicker-mobilefriendly-backdrop-color-bg: #000;\n}\n\n.litepicker-backdrop {\n  display: none;\n  background-color: var(--litepicker-mobilefriendly-backdrop-color-bg);\n  opacity: 0.3;\n  position: fixed;\n  top: 0;\n  right: 0;\n  bottom: 0;\n  left: 0;\n}\n\n.litepicker-open {\n  overflow: hidden;\n}\n\n.litepicker.mobilefriendly[data-plugins*="mobilefriendly"] {\n  transform: translate(-50%, -50%);\n  font-size: 1.1rem;\n  --litepicker-container-months-box-shadow-color: #616161;\n}\n.litepicker.mobilefriendly-portrait {\n  --litepicker-day-width: 13.5vw;\n  --litepicker-month-width: calc(var(--litepicker-day-width) * 7);\n}\n.litepicker.mobilefriendly-landscape {\n  --litepicker-day-width: 5.5vw;\n  --litepicker-month-width: calc(var(--litepicker-day-width) * 7);\n}\n\n.litepicker[data-plugins*="mobilefriendly"] .container__months {\n  overflow: hidden;\n}\n\n.litepicker.mobilefriendly[data-plugins*="mobilefriendly"] .container__months .month-item-header {\n  height: var(--litepicker-day-width);\n}\n\n.litepicker.mobilefriendly[data-plugins*="mobilefriendly"] .container__days > div {\n  height: var(--litepicker-day-width);\n  display: flex;\n  align-items: center;\n  justify-content: center;\n}\n\n\n.litepicker[data-plugins*="mobilefriendly"] .container__months .month-item {\n  transform-origin: center;\n}\n\n.litepicker[data-plugins*="mobilefriendly"] .container__months .month-item.touch-target-next {\n  animation-name: lp-bounce-target-next;\n  animation-duration: .5s;\n  animation-timing-function: ease;\n}\n\n.litepicker[data-plugins*="mobilefriendly"] .container__months .month-item.touch-target-prev {\n  animation-name: lp-bounce-target-prev;\n  animation-duration: .5s;\n  animation-timing-function: ease;\n}\n\n@keyframes lp-bounce-target-next {\n  from {\n    transform: translateX(100px) scale(0.5);\n  }\n  to {\n    transform: translateX(0px) scale(1);\n  }\n}\n\n@keyframes lp-bounce-target-prev {\n  from {\n    transform: translateX(-100px) scale(0.5);\n  }\n  to {\n    transform: translateX(0px) scale(1);\n  }\n}',
      '',
    ]),
      (e.exports = t);
  },
]);
