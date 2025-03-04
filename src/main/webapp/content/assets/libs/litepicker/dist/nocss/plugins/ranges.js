/*!
 *
 * plugins/ranges.js
 * Litepicker v2.0.12 (https://github.com/wakirin/Litepicker)
 * Package: litepicker (https://www.npmjs.com/package/litepicker)
 * License: MIT (https://github.com/wakirin/Litepicker/blob/master/LICENCE.md)
 * Copyright 2019-2021 Rinat G.
 *
 * Hash: fc3887e0bb19d54c36db
 *
 */ !(function (e) {
  var t = {};
  function n(r) {
    if (t[r]) return t[r].exports;
    var o = (t[r] = { i: r, l: !1, exports: {} });
    return e[r].call(o.exports, o, o.exports, n), (o.l = !0), o.exports;
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
        for (var o in e)
          n.d(
            r,
            o,
            function (t) {
              return e[t];
            }.bind(null, o)
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
    n((n.s = 4));
})({
  4: function (e, t, n) {
    'use strict';
    n.r(t);
    n(5);
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
    function o(e) {
      for (var t = 1; t < arguments.length; t++) {
        var n = null != arguments[t] ? arguments[t] : {};
        t % 2
          ? r(Object(n), !0).forEach(function (t) {
              a(e, t, n[t]);
            })
          : Object.getOwnPropertyDescriptors
          ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(n))
          : r(Object(n)).forEach(function (t) {
              Object.defineProperty(e, t, Object.getOwnPropertyDescriptor(n, t));
            });
      }
      return e;
    }
    function a(e, t, n) {
      return t in e ? Object.defineProperty(e, t, { value: n, enumerable: !0, configurable: !0, writable: !0 }) : (e[t] = n), e;
    }
    Litepicker.add('ranges', {
      init: function (e) {
        var t = {
          position: 'left',
          customRanges: {},
          customRangesLabels: ['Today', 'Yesterday', 'Last 7 Days', 'Last 30 Days', 'This Month', 'Last Month'],
          force: !1,
          autoApply: e.options.autoApply,
        };
        (e.options.ranges = o(o({}, t), e.options.ranges)), (e.options.singleMode = !1);
        var n = e.options.ranges;
        if (!Object.keys(n.customRanges).length) {
          var r,
            i = e.DateTime();
          n.customRanges =
            (a((r = {}), n.customRangesLabels[0], [i.clone(), i.clone()]),
            a(r, n.customRangesLabels[1], [i.clone().subtract(1, 'day'), i.clone().subtract(1, 'day')]),
            a(r, n.customRangesLabels[2], [i.clone().subtract(6, 'day'), i]),
            a(r, n.customRangesLabels[3], [i.clone().subtract(29, 'day'), i]),
            a(
              r,
              n.customRangesLabels[4],
              (function (e) {
                var t = e.clone();
                return t.setDate(1), [t, new Date(e.getFullYear(), e.getMonth() + 1, 0)];
              })(i)
            ),
            a(
              r,
              n.customRangesLabels[5],
              (function (e) {
                var t = e.clone();
                return t.setDate(1), t.setMonth(e.getMonth() - 1), [t, new Date(e.getFullYear(), e.getMonth(), 0)];
              })(i)
            ),
            r);
        }
        e.on('render', function (t) {
          var r = document.createElement('div');
          (r.className = 'container__predefined-ranges'),
            (e.ui.dataset.rangesPosition = n.position),
            Object.keys(n.customRanges).forEach(function (o) {
              var a = n.customRanges[o],
                i = document.createElement('button');
              (i.innerText = o),
                (i.tabIndex = t.dataset.plugins.indexOf('keyboardnav') >= 0 ? 1 : -1),
                (i.dataset.start = a[0].getTime()),
                (i.dataset.end = a[1].getTime()),
                i.addEventListener('click', function (t) {
                  var r = t.target;
                  if (r) {
                    var o = e.DateTime(Number(r.dataset.start)),
                      a = e.DateTime(Number(r.dataset.end));
                    n.autoApply
                      ? (e.setDateRange(o, a, n.force), e.emit('ranges.selected', o, a), e.hide())
                      : ((e.datePicked = [o, a]), e.emit('ranges.preselect', o, a)),
                      (!e.options.inlineMode && n.autoApply) || e.gotoDate(o);
                  }
                }),
                r.appendChild(i);
            }),
            t.querySelector('.container__main').prepend(r);
        });
      },
    });
  },
  5: function (e, t, n) {
    e.exports = { litepicker: 'litepicker', containerMain: 'container__main', containerPredefinedRanges: 'container__predefined-ranges' };
  },
});
