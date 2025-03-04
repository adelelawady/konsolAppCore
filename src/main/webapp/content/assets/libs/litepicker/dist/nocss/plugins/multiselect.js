/*!
 *
 * plugins/multiselect.js
 * Litepicker v2.0.12 (https://github.com/wakirin/Litepicker)
 * Package: litepicker (https://www.npmjs.com/package/litepicker)
 * License: MIT (https://github.com/wakirin/Litepicker/blob/master/LICENCE.md)
 * Copyright 2019-2021 Rinat G.
 *
 * Hash: fc3887e0bb19d54c36db
 *
 */ !(function (e) {
  var t = {};
  function r(n) {
    if (t[n]) return t[n].exports;
    var i = (t[n] = { i: n, l: !1, exports: {} });
    return e[n].call(i.exports, i, i.exports, r), (i.l = !0), i.exports;
  }
  (r.m = e),
    (r.c = t),
    (r.d = function (e, t, n) {
      r.o(e, t) || Object.defineProperty(e, t, { enumerable: !0, get: n });
    }),
    (r.r = function (e) {
      'undefined' != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, { value: 'Module' }),
        Object.defineProperty(e, '__esModule', { value: !0 });
    }),
    (r.t = function (e, t) {
      if ((1 & t && (e = r(e)), 8 & t)) return e;
      if (4 & t && 'object' == typeof e && e && e.__esModule) return e;
      var n = Object.create(null);
      if ((r.r(n), Object.defineProperty(n, 'default', { enumerable: !0, value: e }), 2 & t && 'string' != typeof e))
        for (var i in e)
          r.d(
            n,
            i,
            function (t) {
              return e[t];
            }.bind(null, i)
          );
      return n;
    }),
    (r.n = function (e) {
      var t =
        e && e.__esModule
          ? function () {
              return e.default;
            }
          : function () {
              return e;
            };
      return r.d(t, 'a', t), t;
    }),
    (r.o = function (e, t) {
      return Object.prototype.hasOwnProperty.call(e, t);
    }),
    (r.p = ''),
    r((r.s = 6));
})({
  6: function (e, t, r) {
    'use strict';
    r.r(t);
    r(7);
    function n(e) {
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
          var r = Object.prototype.toString.call(e).slice(8, -1);
          'Object' === r && e.constructor && (r = e.constructor.name);
          if ('Map' === r || 'Set' === r) return Array.from(e);
          if ('Arguments' === r || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(r)) return i(e, t);
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
      for (var r = 0, n = new Array(t); r < t; r++) n[r] = e[r];
      return n;
    }
    function o(e, t) {
      var r = Object.keys(e);
      if (Object.getOwnPropertySymbols) {
        var n = Object.getOwnPropertySymbols(e);
        t &&
          (n = n.filter(function (t) {
            return Object.getOwnPropertyDescriptor(e, t).enumerable;
          })),
          r.push.apply(r, n);
      }
      return r;
    }
    function l(e) {
      for (var t = 1; t < arguments.length; t++) {
        var r = null != arguments[t] ? arguments[t] : {};
        t % 2
          ? o(Object(r), !0).forEach(function (t) {
              u(e, t, r[t]);
            })
          : Object.getOwnPropertyDescriptors
          ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(r))
          : o(Object(r)).forEach(function (t) {
              Object.defineProperty(e, t, Object.getOwnPropertyDescriptor(r, t));
            });
      }
      return e;
    }
    function u(e, t, r) {
      return t in e ? Object.defineProperty(e, t, { value: r, enumerable: !0, configurable: !0, writable: !0 }) : (e[t] = r), e;
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
            r = e.ui.querySelector('.preview-date-range');
          if (r && t > 0) {
            var n = e.pluralSelector(t),
              i = e.options.tooltipText[n] ? e.options.tooltipText[n] : '['.concat(n, ']'),
              o = ''.concat(t, ' ').concat(i);
            r.innerText = o;
          }
        };
        e.on('before:show', function () {
          e.preMultipleDates = n(e.multipleDates);
        }),
          e.on('show', function () {
            t();
          }),
          e.on('before:click', function (r) {
            if (r.classList.contains('day-item')) {
              if (((e.preventClick = !0), r.classList.contains('is-locked'))) return void r.blur();
              var n = Number(r.dataset.time);
              r.classList.contains('is-selected')
                ? ((e.preMultipleDates = e.preMultipleDates.filter(function (e) {
                    return e !== n;
                  })),
                  e.emit('multiselect.deselect', e.DateTime(n)))
                : ((e.preMultipleDates[e.preMultipleDates.length] = n), e.emit('multiselect.select', e.DateTime(n))),
                e.options.autoApply && e.emit('button:apply'),
                e.render(),
                t();
            }
          }),
          e.on('render:day', function (t) {
            var r = e.preMultipleDates.filter(function (e) {
                return e === Number(t.dataset.time);
              }).length,
              n = Number(e.options.multiselect.max);
            r ? t.classList.add('is-selected') : n && e.preMultipleDates.length >= n && t.classList.add('is-locked');
          }),
          e.on('button:cancel', function () {
            e.preMultipleDates.length = 0;
          }),
          e.on('button:apply', function () {
            e.multipleDates = n(e.preMultipleDates).sort(function (e, t) {
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
              r = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : ',';
            return e.multipleDates
              .map(function (r) {
                return e.DateTime(r).format(t);
              })
              .join(r);
          });
      },
    });
  },
  7: function (e, t, r) {
    e.exports = {
      litepicker: 'litepicker',
      containerDays: 'container__days',
      dayItem: 'day-item',
      isLocked: 'is-locked',
      isSelected: 'is-selected',
    };
  },
});
