/*!
 *
 *     Litepicker v1.5.7 (https://github.com/wakirin/Litepicker)
 *     Package: litepicker (https://www.npmjs.com/package/litepicker)
 *     License: MIT (https://github.com/wakirin/Litepicker/blob/master/LICENCE.md)
 *     Copyright 2019-2020 Rinat G.
 *
 *     Hash: 85b7ce11883a11b98abb
 *     Generated on: 1592979305748
 *
 */
!(function (t, e) {
  'object' == typeof exports && 'object' == typeof module
    ? (module.exports = e())
    : 'function' == typeof define && define.amd
    ? define('Litepicker', [], e)
    : 'object' == typeof exports
    ? (exports.Litepicker = e())
    : (t.Litepicker = e());
})(window, function () {
  return (function (t) {
    var e = {};
    function i(o) {
      if (e[o]) return e[o].exports;
      var n = (e[o] = { i: o, l: !1, exports: {} });
      return t[o].call(n.exports, n, n.exports, i), (n.l = !0), n.exports;
    }
    return (
      (i.m = t),
      (i.c = e),
      (i.d = function (t, e, o) {
        i.o(t, e) || Object.defineProperty(t, e, { enumerable: !0, get: o });
      }),
      (i.r = function (t) {
        'undefined' != typeof Symbol && Symbol.toStringTag && Object.defineProperty(t, Symbol.toStringTag, { value: 'Module' }),
          Object.defineProperty(t, '__esModule', { value: !0 });
      }),
      (i.t = function (t, e) {
        if ((1 & e && (t = i(t)), 8 & e)) return t;
        if (4 & e && 'object' == typeof t && t && t.__esModule) return t;
        var o = Object.create(null);
        if ((i.r(o), Object.defineProperty(o, 'default', { enumerable: !0, value: t }), 2 & e && 'string' != typeof t))
          for (var n in t)
            i.d(
              o,
              n,
              function (e) {
                return t[e];
              }.bind(null, n)
            );
        return o;
      }),
      (i.n = function (t) {
        var e =
          t && t.__esModule
            ? function () {
                return t.default;
              }
            : function () {
                return t;
              };
        return i.d(e, 'a', e), e;
      }),
      (i.o = function (t, e) {
        return Object.prototype.hasOwnProperty.call(t, e);
      }),
      (i.p = ''),
      i((i.s = 4))
    );
  })([
    function (t, e, i) {
      'use strict';
      Object.defineProperty(e, '__esModule', { value: !0 });
      var o = (function () {
        function t(e, i, o) {
          void 0 === e && (e = null),
            void 0 === i && (i = null),
            void 0 === o && (o = 'en-US'),
            (this.dateInstance = i ? t.parseDateTime(e, i, o) : e ? t.parseDateTime(e) : t.parseDateTime(new Date())),
            (this.lang = o);
        }
        return (
          (t.parseDateTime = function (e, i, o) {
            if ((void 0 === i && (i = 'YYYY-MM-DD'), void 0 === o && (o = 'en-US'), !e)) return new Date(NaN);
            if (e instanceof Date) return new Date(e);
            if (e instanceof t) return e.clone().getDateInstance();
            if (/^-?\d{10,}$/.test(e)) return t.getDateZeroTime(new Date(Number(e)));
            if ('string' == typeof e) {
              for (var n = [], s = null; null != (s = t.regex.exec(i)); ) '\\' !== s[1] && n.push(s);
              if (n.length) {
                var r = { year: null, month: null, shortMonth: null, longMonth: null, day: null, value: '' };
                n[0].index > 0 && (r.value += '.*?');
                for (var a = 0, l = Object.entries(n); a < l.length; a++) {
                  var p = l[a],
                    c = p[0],
                    h = p[1],
                    d = Number(c),
                    u = t.formatPatterns(h[0], o),
                    m = u.group,
                    f = u.pattern;
                  (r[m] = d + 1), (r.value += f), (r.value += '.*?');
                }
                var g = new RegExp('^' + r.value + '$');
                if (g.test(e)) {
                  var y = g.exec(e),
                    k = Number(y[r.year]),
                    v = null;
                  r.month
                    ? (v = Number(y[r.month]) - 1)
                    : r.shortMonth
                    ? (v = t.shortMonths(o).indexOf(y[r.shortMonth]))
                    : r.longMonth && (v = t.longMonths(o).indexOf(y[r.longMonth]));
                  var D = Number(y[r.day]) || 1;
                  return new Date(k, v, D, 0, 0, 0, 0);
                }
              }
            }
            return t.getDateZeroTime(new Date(e));
          }),
          (t.convertArray = function (e, i) {
            return e.map(function (e) {
              return e instanceof Array
                ? e.map(function (e) {
                    return new t(e, i);
                  })
                : new t(e, i);
            });
          }),
          (t.getDateZeroTime = function (t) {
            return new Date(t.getFullYear(), t.getMonth(), t.getDate(), 0, 0, 0, 0);
          }),
          (t.shortMonths = function (e) {
            return t.MONTH_JS.map(function (t) {
              return new Date(2019, t).toLocaleString(e, { month: 'short' });
            });
          }),
          (t.longMonths = function (e) {
            return t.MONTH_JS.map(function (t) {
              return new Date(2019, t).toLocaleString(e, { month: 'long' });
            });
          }),
          (t.formatPatterns = function (e, i) {
            switch (e) {
              case 'YY':
              case 'YYYY':
                return { group: 'year', pattern: '(\\d{' + e.length + '})' };
              case 'M':
                return { group: 'month', pattern: '(\\d{1,2})' };
              case 'MM':
                return { group: 'month', pattern: '(\\d{2})' };
              case 'MMM':
                return { group: 'shortMonth', pattern: '(' + t.shortMonths(i).join('|') + ')' };
              case 'MMMM':
                return { group: 'longMonth', pattern: '(' + t.longMonths(i).join('|') + ')' };
              case 'D':
                return { group: 'day', pattern: '(\\d{1,2})' };
              case 'DD':
                return { group: 'day', pattern: '(\\d{2})' };
            }
          }),
          (t.prototype.getDateInstance = function () {
            return this.dateInstance;
          }),
          (t.prototype.toLocaleString = function (t, e) {
            return this.dateInstance.toLocaleString(t, e);
          }),
          (t.prototype.toDateString = function () {
            return this.dateInstance.toDateString();
          }),
          (t.prototype.getSeconds = function () {
            return this.dateInstance.getSeconds();
          }),
          (t.prototype.getDay = function () {
            return this.dateInstance.getDay();
          }),
          (t.prototype.getTime = function () {
            return this.dateInstance.getTime();
          }),
          (t.prototype.getDate = function () {
            return this.dateInstance.getDate();
          }),
          (t.prototype.getMonth = function () {
            return this.dateInstance.getMonth();
          }),
          (t.prototype.getFullYear = function () {
            return this.dateInstance.getFullYear();
          }),
          (t.prototype.setMonth = function (t) {
            return this.dateInstance.setMonth(t);
          }),
          (t.prototype.setHours = function (t, e, i, o) {
            void 0 === t && (t = 0),
              void 0 === e && (e = 0),
              void 0 === i && (i = 0),
              void 0 === o && (o = 0),
              this.dateInstance.setHours(t, e, i, o);
          }),
          (t.prototype.setSeconds = function (t) {
            return this.dateInstance.setSeconds(t);
          }),
          (t.prototype.setDate = function (t) {
            return this.dateInstance.setDate(t);
          }),
          (t.prototype.setFullYear = function (t) {
            return this.dateInstance.setFullYear(t);
          }),
          (t.prototype.getWeek = function (t) {
            var e = new Date(this.timestamp()),
              i = (this.getDay() + (7 - t)) % 7;
            e.setDate(e.getDate() - i);
            var o = e.getTime();
            return (
              e.setMonth(0, 1), e.getDay() !== t && e.setMonth(0, 1 + ((4 - e.getDay() + 7) % 7)), 1 + Math.ceil((o - e.getTime()) / 6048e5)
            );
          }),
          (t.prototype.clone = function () {
            return new t(this.getDateInstance());
          }),
          (t.prototype.isBetween = function (t, e, i) {
            switch ((void 0 === i && (i = '()'), i)) {
              default:
              case '()':
                return this.timestamp() > t.getTime() && this.timestamp() < e.getTime();
              case '[)':
                return this.timestamp() >= t.getTime() && this.timestamp() < e.getTime();
              case '(]':
                return this.timestamp() > t.getTime() && this.timestamp() <= e.getTime();
              case '[]':
                return this.timestamp() >= t.getTime() && this.timestamp() <= e.getTime();
            }
          }),
          (t.prototype.isBefore = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return t.getTime() > this.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime() >
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(t.getFullYear(), t.getMonth(), 1).getTime() > new Date(this.getFullYear(), this.getMonth(), 1).getTime();
              case 'year':
              case 'years':
                return t.getFullYear() > this.getFullYear();
            }
            throw new Error('isBefore: Invalid unit!');
          }),
          (t.prototype.isSameOrBefore = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return t.getTime() >= this.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime() >=
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(t.getFullYear(), t.getMonth(), 1).getTime() >= new Date(this.getFullYear(), this.getMonth(), 1).getTime();
            }
            throw new Error('isSameOrBefore: Invalid unit!');
          }),
          (t.prototype.isAfter = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return this.getTime() > t.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime() >
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(this.getFullYear(), this.getMonth(), 1).getTime() > new Date(t.getFullYear(), t.getMonth(), 1).getTime();
              case 'year':
              case 'years':
                return this.getFullYear() > t.getFullYear();
            }
            throw new Error('isAfter: Invalid unit!');
          }),
          (t.prototype.isSameOrAfter = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return this.getTime() >= t.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime() >=
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(this.getFullYear(), this.getMonth(), 1).getTime() >= new Date(t.getFullYear(), t.getMonth(), 1).getTime();
            }
            throw new Error('isSameOrAfter: Invalid unit!');
          }),
          (t.prototype.isSame = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return this.getTime() === t.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime() ===
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(this.getFullYear(), this.getMonth(), 1).getTime() === new Date(t.getFullYear(), t.getMonth(), 1).getTime();
            }
            throw new Error('isSame: Invalid unit!');
          }),
          (t.prototype.add = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                this.setSeconds(this.getSeconds() + t);
                break;
              case 'day':
              case 'days':
                this.setDate(this.getDate() + t);
                break;
              case 'month':
              case 'months':
                this.setMonth(this.getMonth() + t);
            }
            return this;
          }),
          (t.prototype.subtract = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                this.setSeconds(this.getSeconds() - t);
                break;
              case 'day':
              case 'days':
                this.setDate(this.getDate() - t);
                break;
              case 'month':
              case 'months':
                this.setMonth(this.getMonth() - t);
            }
            return this;
          }),
          (t.prototype.diff = function (t, e) {
            void 0 === e && (e = 'seconds');
            switch (e) {
              default:
              case 'second':
              case 'seconds':
                return this.getTime() - t.getTime();
              case 'day':
              case 'days':
                return Math.round((this.timestamp() - t.getTime()) / 864e5);
              case 'month':
              case 'months':
            }
          }),
          (t.prototype.format = function (e, i) {
            void 0 === i && (i = 'en-US');
            for (var o = '', n = [], s = null; null != (s = t.regex.exec(e)); ) '\\' !== s[1] && n.push(s);
            if (n.length) {
              n[0].index > 0 && (o += e.substring(0, n[0].index));
              for (var r = 0, a = Object.entries(n); r < a.length; r++) {
                var l = a[r],
                  p = l[0],
                  c = l[1],
                  h = Number(p);
                (o += this.formatTokens(c[0], i)),
                  n[h + 1] && (o += e.substring(c.index + c[0].length, n[h + 1].index)),
                  h === n.length - 1 && (o += e.substring(c.index + c[0].length));
              }
            }
            return o.replace(/\\/g, '');
          }),
          (t.prototype.timestamp = function () {
            return new Date(this.getFullYear(), this.getMonth(), this.getDate(), 0, 0, 0, 0).getTime();
          }),
          (t.prototype.formatTokens = function (e, i) {
            switch (e) {
              case 'YY':
                return String(this.getFullYear()).slice(-2);
              case 'YYYY':
                return String(this.getFullYear());
              case 'M':
                return String(this.getMonth() + 1);
              case 'MM':
                return ('0' + (this.getMonth() + 1)).slice(-2);
              case 'MMM':
                return t.shortMonths(i)[this.getMonth()];
              case 'MMMM':
                return t.longMonths(i)[this.getMonth()];
              case 'D':
                return String(this.getDate());
              case 'DD':
                return ('0' + this.getDate()).slice(-2);
              default:
                return '';
            }
          }),
          (t.regex = /(\\)?(Y{2,4}|M{1,4}|D{1,2}|d{1,4})/g),
          (t.MONTH_JS = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]),
          t
        );
      })();
      e.DateTime = o;
    },
    function (t, e, i) {
      var o = i(6);
      'string' == typeof o && (o = [[t.i, o, '']]);
      var n = {
        insert: function (t) {
          var e = document.querySelector('head'),
            i = window._lastElementInsertedByStyleLoader;
          window.disableLitepickerStyles ||
            (i ? (i.nextSibling ? e.insertBefore(t, i.nextSibling) : e.appendChild(t)) : e.insertBefore(t, e.firstChild),
            (window._lastElementInsertedByStyleLoader = t));
        },
        singleton: !1,
      };
      i(8)(o, n);
      o.locals && (t.exports = o.locals);
    },
    function (t, e, i) {
      'use strict';
      function o() {
        return window.matchMedia('(orientation: portrait)').matches ? 'portrait' : 'landscape';
      }
      Object.defineProperty(e, '__esModule', { value: !0 }),
        (e.isMobile = function () {
          var t = 'portrait' === o();
          return window.matchMedia('(max-device-' + (t ? 'width' : 'height') + ': 480px)').matches;
        }),
        (e.getOrientation = o),
        (e.findNestedMonthItem = function (t) {
          for (var e = t.parentNode.childNodes, i = 0; i < e.length; i += 1) {
            if (e.item(i) === t) return i;
          }
          return 0;
        });
    },
    function (t, e, i) {
      'use strict';
      var o,
        n =
          (this && this.__extends) ||
          ((o = function (t, e) {
            return (o =
              Object.setPrototypeOf ||
              ({ __proto__: [] } instanceof Array &&
                function (t, e) {
                  t.__proto__ = e;
                }) ||
              function (t, e) {
                for (var i in e) e.hasOwnProperty(i) && (t[i] = e[i]);
              })(t, e);
          }),
          function (t, e) {
            function i() {
              this.constructor = t;
            }
            o(t, e), (t.prototype = null === e ? Object.create(e) : ((i.prototype = e.prototype), new i()));
          }),
        s =
          (this && this.__assign) ||
          function () {
            return (s =
              Object.assign ||
              function (t) {
                for (var e, i = 1, o = arguments.length; i < o; i++)
                  for (var n in (e = arguments[i])) Object.prototype.hasOwnProperty.call(e, n) && (t[n] = e[n]);
                return t;
              }).apply(this, arguments);
          },
        r =
          (this && this.__importStar) ||
          function (t) {
            if (t && t.__esModule) return t;
            var e = {};
            if (null != t) for (var i in t) Object.hasOwnProperty.call(t, i) && (e[i] = t[i]);
            return (e.default = t), e;
          };
      Object.defineProperty(e, '__esModule', { value: !0 });
      var a = i(5),
        l = i(0),
        p = r(i(1)),
        c = i(2),
        h = (function (t) {
          function e(e) {
            var i = t.call(this) || this;
            (i.options = s(s({}, i.options), e.element.dataset)),
              Object.keys(i.options).forEach(function (t) {
                ('true' !== i.options[t] && 'false' !== i.options[t]) || (i.options[t] = 'true' === i.options[t]);
              });
            var o = s(s({}, i.options.dropdowns), e.dropdowns),
              n = s(s({}, i.options.buttonText), e.buttonText),
              r = s(s({}, i.options.tooltipText), e.tooltipText);
            (i.options = s(s({}, i.options), e)),
              (i.options.dropdowns = s({}, o)),
              (i.options.buttonText = s({}, n)),
              (i.options.tooltipText = s({}, r)),
              i.options.elementEnd || (i.options.allowRepick = !1),
              i.options.lockDays.length && (i.options.lockDays = l.DateTime.convertArray(i.options.lockDays, i.options.lockDaysFormat)),
              i.options.bookedDays.length &&
                (i.options.bookedDays = l.DateTime.convertArray(i.options.bookedDays, i.options.bookedDaysFormat)),
              i.options.highlightedDays.length &&
                (i.options.highlightedDays = l.DateTime.convertArray(i.options.highlightedDays, i.options.highlightedDaysFormat)),
              !i.options.hotelMode || 'bookedDaysInclusivity' in e || (i.options.bookedDaysInclusivity = '[)'),
              !i.options.hotelMode || 'disallowBookedDaysInRange' in e || (i.options.disallowBookedDaysInRange = !0),
              !i.options.hotelMode || 'selectForward' in e || (i.options.selectForward = !0);
            var a = i.parseInput(),
              p = a[0],
              c = a[1];
            i.options.startDate &&
              (i.options.singleMode || i.options.endDate) &&
              (p = new l.DateTime(i.options.startDate, i.options.format, i.options.lang)),
              p && i.options.endDate && (c = new l.DateTime(i.options.endDate, i.options.format, i.options.lang)),
              p instanceof l.DateTime && !isNaN(p.getTime()) && (i.options.startDate = p),
              i.options.startDate && c instanceof l.DateTime && !isNaN(c.getTime()) && (i.options.endDate = c),
              !i.options.singleMode || i.options.startDate instanceof l.DateTime || (i.options.startDate = null),
              i.options.singleMode ||
                (i.options.startDate instanceof l.DateTime && i.options.endDate instanceof l.DateTime) ||
                ((i.options.startDate = null), (i.options.endDate = null));
            for (var h = 0; h < i.options.numberOfMonths; h += 1) {
              var d = i.options.startDate instanceof l.DateTime ? i.options.startDate.clone() : new l.DateTime();
              d.setDate(1), d.setMonth(d.getMonth() + h), (i.calendars[h] = d);
            }
            if (i.options.showTooltip)
              if (i.options.tooltipPluralSelector) i.pluralSelector = i.options.tooltipPluralSelector;
              else
                try {
                  var u = new Intl.PluralRules(i.options.lang);
                  i.pluralSelector = u.select.bind(u);
                } catch (t) {
                  i.pluralSelector = function (t) {
                    return 0 === Math.abs(t) ? 'one' : 'other';
                  };
                }
            return i.loadPolyfillsForIE11(), i.onInit(), i;
          }
          return (
            n(e, t),
            (e.prototype.onInit = function () {
              var t = this;
              if (
                (document.addEventListener(
                  'click',
                  function (e) {
                    return t.onClick(e);
                  },
                  !0
                ),
                (this.picker = document.createElement('div')),
                (this.picker.className = p.litepicker),
                (this.picker.style.display = 'none'),
                this.picker.addEventListener(
                  'mouseenter',
                  function (e) {
                    return t.onMouseEnter(e);
                  },
                  !0
                ),
                this.picker.addEventListener(
                  'mouseleave',
                  function (e) {
                    return t.onMouseLeave(e);
                  },
                  !1
                ),
                this.options.autoRefresh
                  ? (this.options.element instanceof HTMLElement &&
                      this.options.element.addEventListener(
                        'keyup',
                        function (e) {
                          return t.onInput(e);
                        },
                        !0
                      ),
                    this.options.elementEnd instanceof HTMLElement &&
                      this.options.elementEnd.addEventListener(
                        'keyup',
                        function (e) {
                          return t.onInput(e);
                        },
                        !0
                      ))
                  : (this.options.element instanceof HTMLElement &&
                      this.options.element.addEventListener(
                        'change',
                        function (e) {
                          return t.onInput(e);
                        },
                        !0
                      ),
                    this.options.elementEnd instanceof HTMLElement &&
                      this.options.elementEnd.addEventListener(
                        'change',
                        function (e) {
                          return t.onInput(e);
                        },
                        !0
                      )),
                this.options.moduleNavKeyboard)
              ) {
                if ('function' != typeof this.enableModuleNavKeyboard)
                  throw new Error(
                    'moduleNavKeyboard is on but library does not included. See https://github.com/wakirin/litepicker-module-navkeyboard.'
                  );
                this.enableModuleNavKeyboard.call(this, this);
              }
              this.render(),
                this.options.parentEl
                  ? this.options.parentEl instanceof HTMLElement
                    ? this.options.parentEl.appendChild(this.picker)
                    : document.querySelector(this.options.parentEl).appendChild(this.picker)
                  : this.options.inlineMode
                  ? this.options.element instanceof HTMLInputElement
                    ? this.options.element.parentNode.appendChild(this.picker)
                    : this.options.element.appendChild(this.picker)
                  : document.body.appendChild(this.picker),
                this.options.mobileFriendly &&
                  ((this.backdrop = document.createElement('div')),
                  (this.backdrop.className = p.litepickerBackdrop),
                  this.backdrop.addEventListener('click', this.hide()),
                  this.options.element && this.options.element.parentNode && this.options.element.parentNode.appendChild(this.backdrop),
                  window.addEventListener('orientationchange', function (e) {
                    var i = function () {
                      if (c.isMobile() && t.isShowning()) {
                        switch (c.getOrientation()) {
                          case 'landscape':
                            (t.options.numberOfMonths = 2), (t.options.numberOfColumns = 2);
                            break;
                          default:
                            (t.options.numberOfMonths = 1), (t.options.numberOfColumns = 1);
                        }
                        if ((t.render(), !t.options.inlineMode)) {
                          var e = t.picker.getBoundingClientRect();
                          (t.picker.style.top = 'calc(50% - ' + e.height / 2 + 'px)'),
                            (t.picker.style.left = 'calc(50% - ' + e.width / 2 + 'px)');
                        }
                      }
                      window.removeEventListener('resize', i);
                    };
                    window.addEventListener('resize', i);
                  })),
                this.options.inlineMode &&
                  (this.show(),
                  this.options.mobileFriendly &&
                    c.isMobile() &&
                    (window.dispatchEvent(new Event('orientationchange')), window.dispatchEvent(new Event('resize')))),
                this.updateInput();
            }),
            (e.prototype.parseInput = function () {
              var t = this.options.delimiter,
                e = new RegExp('' + t),
                i = this.options.element instanceof HTMLInputElement ? this.options.element.value.split(t) : [];
              if (this.options.elementEnd) {
                if (
                  this.options.element instanceof HTMLInputElement &&
                  this.options.element.value.length &&
                  this.options.elementEnd instanceof HTMLInputElement &&
                  this.options.elementEnd.value.length
                )
                  return [
                    new l.DateTime(this.options.element.value, this.options.format),
                    new l.DateTime(this.options.elementEnd.value, this.options.format),
                  ];
              } else if (this.options.singleMode) {
                if (this.options.element instanceof HTMLInputElement && this.options.element.value.length)
                  return [new l.DateTime(this.options.element.value, this.options.format)];
              } else if (
                this.options.element instanceof HTMLInputElement &&
                e.test(this.options.element.value) &&
                i.length &&
                i.length % 2 == 0
              ) {
                var o = i.slice(0, i.length / 2).join(t),
                  n = i.slice(i.length / 2).join(t);
                return [new l.DateTime(o, this.options.format), new l.DateTime(n, this.options.format)];
              }
              return [];
            }),
            (e.prototype.updateInput = function () {
              if (this.options.element instanceof HTMLInputElement) {
                if (this.options.singleMode && this.options.startDate)
                  this.options.element.value = this.options.startDate.format(this.options.format, this.options.lang);
                else if (!this.options.singleMode && this.options.startDate && this.options.endDate) {
                  var t = this.options.startDate.format(this.options.format, this.options.lang),
                    e = this.options.endDate.format(this.options.format, this.options.lang);
                  this.options.elementEnd
                    ? ((this.options.element.value = t), (this.options.elementEnd.value = e))
                    : (this.options.element.value = '' + t + this.options.delimiter + e);
                }
                this.options.startDate ||
                  this.options.endDate ||
                  ((this.options.element.value = ''), this.options.elementEnd && (this.options.elementEnd.value = ''));
              }
            }),
            (e.prototype.isSamePicker = function (t) {
              return t.closest('.' + p.litepicker) === this.picker;
            }),
            (e.prototype.shouldShown = function (t) {
              return t === this.options.element || (this.options.elementEnd && t === this.options.elementEnd);
            }),
            (e.prototype.shouldResetDatePicked = function () {
              return this.options.singleMode || 2 === this.datePicked.length;
            }),
            (e.prototype.shouldSwapDatePicked = function () {
              return 2 === this.datePicked.length && this.datePicked[0].getTime() > this.datePicked[1].getTime();
            }),
            (e.prototype.shouldCheckLockDays = function () {
              return this.options.disallowLockDaysInRange && this.options.lockDays.length && 2 === this.datePicked.length;
            }),
            (e.prototype.shouldCheckBookedDays = function () {
              return this.options.disallowBookedDaysInRange && this.options.bookedDays.length && 2 === this.datePicked.length;
            }),
            (e.prototype.onClick = function (t) {
              var e = this,
                i = t.target;
              if (i && this.picker)
                if (this.shouldShown(i)) this.show(i);
                else if (i.closest('.' + p.litepicker)) {
                  if (i.classList.contains(p.dayItem)) {
                    if ((t.preventDefault(), !this.isSamePicker(i))) return;
                    if (i.classList.contains(p.isLocked)) return;
                    if (i.classList.contains(p.isBooked)) return;
                    if (
                      (this.shouldResetDatePicked() && (this.datePicked.length = 0),
                      (this.datePicked[this.datePicked.length] = new l.DateTime(i.dataset.time)),
                      this.shouldSwapDatePicked())
                    ) {
                      var o = this.datePicked[1].clone();
                      (this.datePicked[1] = this.datePicked[0].clone()), (this.datePicked[0] = o.clone());
                    }
                    if (this.shouldCheckLockDays()) {
                      var n = this.options.lockDaysInclusivity;
                      this.options.lockDays.filter(function (t) {
                        return t instanceof Array
                          ? t[0].isBetween(e.datePicked[0], e.datePicked[1], n) || t[1].isBetween(e.datePicked[0], e.datePicked[1], n)
                          : t.isBetween(e.datePicked[0], e.datePicked[1], n);
                      }).length &&
                        ((this.datePicked.length = 0),
                        'function' == typeof this.options.onError && this.options.onError.call(this, 'INVALID_RANGE'));
                    }
                    if (this.shouldCheckBookedDays()) {
                      var s = this.options.bookedDaysInclusivity;
                      this.options.hotelMode && 2 === this.datePicked.length && (s = '()');
                      var r = this.options.bookedDays.filter(function (t) {
                          return t instanceof Array
                            ? t[0].isBetween(e.datePicked[0], e.datePicked[1], s) || t[1].isBetween(e.datePicked[0], e.datePicked[1], s)
                            : t.isBetween(e.datePicked[0], e.datePicked[1]);
                        }).length,
                        a = this.options.anyBookedDaysAsCheckout && 1 === this.datePicked.length;
                      r &&
                        !a &&
                        ((this.datePicked.length = 0),
                        'function' == typeof this.options.onError && this.options.onError.call(this, 'INVALID_RANGE'));
                    }
                    return (
                      this.render(),
                      void (
                        this.options.autoApply &&
                        (this.options.singleMode && this.datePicked.length
                          ? (this.setDate(this.datePicked[0]), this.hide())
                          : this.options.singleMode ||
                            2 !== this.datePicked.length ||
                            (this.setDateRange(this.datePicked[0], this.datePicked[1]), this.hide()))
                      )
                    );
                  }
                  if (i.classList.contains(p.buttonPreviousMonth)) {
                    if ((t.preventDefault(), !this.isSamePicker(i))) return;
                    var h = 0,
                      d = this.options.moveByOneMonth ? 1 : this.options.numberOfMonths;
                    if (this.options.splitView) {
                      var u = i.closest('.' + p.monthItem);
                      (h = c.findNestedMonthItem(u)), (d = 1);
                    }
                    return (
                      this.calendars[h].setMonth(this.calendars[h].getMonth() - d),
                      this.gotoDate(this.calendars[h], h),
                      void ('function' == typeof this.options.onChangeMonth && this.options.onChangeMonth.call(this, this.calendars[h], h))
                    );
                  }
                  if (i.classList.contains(p.buttonNextMonth)) {
                    if ((t.preventDefault(), !this.isSamePicker(i))) return;
                    (h = 0), (d = this.options.moveByOneMonth ? 1 : this.options.numberOfMonths);
                    if (this.options.splitView) {
                      u = i.closest('.' + p.monthItem);
                      (h = c.findNestedMonthItem(u)), (d = 1);
                    }
                    return (
                      this.calendars[h].setMonth(this.calendars[h].getMonth() + d),
                      this.gotoDate(this.calendars[h], h),
                      void ('function' == typeof this.options.onChangeMonth && this.options.onChangeMonth.call(this, this.calendars[h], h))
                    );
                  }
                  if (i.classList.contains(p.buttonCancel)) {
                    if ((t.preventDefault(), !this.isSamePicker(i))) return;
                    this.hide();
                  }
                  if (i.classList.contains(p.buttonApply)) {
                    if ((t.preventDefault(), !this.isSamePicker(i))) return;
                    this.options.singleMode && this.datePicked.length
                      ? this.setDate(this.datePicked[0])
                      : this.options.singleMode ||
                        2 !== this.datePicked.length ||
                        this.setDateRange(this.datePicked[0], this.datePicked[1]),
                      this.hide();
                  }
                } else this.hide();
            }),
            (e.prototype.showTooltip = function (t, e) {
              var i = this.picker.querySelector('.' + p.containerTooltip);
              (i.style.visibility = 'visible'), (i.innerHTML = e);
              var o = this.picker.getBoundingClientRect(),
                n = i.getBoundingClientRect(),
                s = t.getBoundingClientRect(),
                r = s.top,
                a = s.left;
              if (this.options.inlineMode && this.options.parentEl) {
                var l = this.picker.parentNode.getBoundingClientRect();
                (r -= l.top), (a -= l.left);
              } else (r -= o.top), (a -= o.left);
              (r -= n.height),
                (a -= n.width / 2),
                (a += s.width / 2),
                (i.style.top = r + 'px'),
                (i.style.left = a + 'px'),
                'function' == typeof this.options.onShowTooltip && this.options.onShowTooltip.call(this, i, t);
            }),
            (e.prototype.hideTooltip = function () {
              this.picker.querySelector('.' + p.containerTooltip).style.visibility = 'hidden';
            }),
            (e.prototype.shouldAllowMouseEnter = function (t) {
              return !this.options.singleMode && !t.classList.contains(p.isLocked) && !t.classList.contains(p.isBooked);
            }),
            (e.prototype.shouldAllowRepick = function () {
              return this.options.elementEnd && this.options.allowRepick && this.options.startDate && this.options.endDate;
            }),
            (e.prototype.isDayItem = function (t) {
              return t.classList.contains(p.dayItem);
            }),
            (e.prototype.onMouseEnter = function (t) {
              var e = this,
                i = t.target;
              if (
                this.isDayItem(i) &&
                ('function' == typeof this.options.onDayHover &&
                  this.options.onDayHover.call(this, l.DateTime.parseDateTime(i.dataset.time), i.classList.toString().split(/\s/), i),
                this.shouldAllowMouseEnter(i))
              ) {
                if (
                  (this.shouldAllowRepick() &&
                    (this.triggerElement === this.options.element
                      ? (this.datePicked[0] = this.options.endDate.clone())
                      : this.triggerElement === this.options.elementEnd && (this.datePicked[0] = this.options.startDate.clone())),
                  1 !== this.datePicked.length)
                )
                  return;
                var o = this.picker.querySelector('.' + p.dayItem + '[data-time="' + this.datePicked[0].getTime() + '"]'),
                  n = this.datePicked[0].clone(),
                  s = new l.DateTime(i.dataset.time),
                  r = !1;
                if (n.getTime() > s.getTime()) {
                  var a = n.clone();
                  (n = s.clone()), (s = a.clone()), (r = !0);
                }
                if (
                  (Array.prototype.slice.call(this.picker.querySelectorAll('.' + p.dayItem)).forEach(function (t) {
                    var i = new l.DateTime(t.dataset.time),
                      o = e.renderDay(i);
                    i.isBetween(n, s) && o.classList.add(p.isInRange), (t.className = o.className);
                  }),
                  i.classList.add(p.isEndDate),
                  r
                    ? (o && o.classList.add(p.isFlipped), i.classList.add(p.isFlipped))
                    : (o && o.classList.remove(p.isFlipped), i.classList.remove(p.isFlipped)),
                  this.options.showTooltip)
                ) {
                  var c = s.diff(n, 'day');
                  if ((this.options.hotelMode || (c += 1), c > 0)) {
                    var h = this.pluralSelector(c),
                      d = c + ' ' + (this.options.tooltipText[h] ? this.options.tooltipText[h] : '[' + h + ']');
                    this.showTooltip(i, d);
                  } else this.hideTooltip();
                }
              }
            }),
            (e.prototype.onMouseLeave = function (t) {
              t.target;
              this.options.allowRepick &&
                (!this.options.allowRepick || this.options.startDate || this.options.endDate) &&
                ((this.datePicked.length = 0), this.render());
            }),
            (e.prototype.onInput = function (t) {
              var e = this.parseInput(),
                i = e[0],
                o = e[1],
                n = this.options.format;
              if (
                this.options.elementEnd
                  ? i instanceof l.DateTime &&
                    o instanceof l.DateTime &&
                    i.format(n) === this.options.element.value &&
                    o.format(n) === this.options.elementEnd.value
                  : this.options.singleMode
                  ? i instanceof l.DateTime && i.format(n) === this.options.element.value
                  : i instanceof l.DateTime &&
                    o instanceof l.DateTime &&
                    '' + i.format(n) + this.options.delimiter + o.format(n) === this.options.element.value
              ) {
                if (o && i.getTime() > o.getTime()) {
                  var s = i.clone();
                  (i = o.clone()), (o = s.clone());
                }
                (this.options.startDate = new l.DateTime(i, this.options.format, this.options.lang)),
                  o && (this.options.endDate = new l.DateTime(o, this.options.format, this.options.lang)),
                  this.updateInput(),
                  this.render();
                var r = i.clone(),
                  a = 0;
                (this.options.elementEnd ? i.format(n) === t.target.value : t.target.value.startsWith(i.format(n))) ||
                  ((r = o.clone()), (a = this.options.numberOfMonths - 1)),
                  'function' == typeof this.options.onSelect && this.options.onSelect.call(this, this.getStartDate(), this.getEndDate()),
                  this.gotoDate(r, a);
              }
            }),
            (e.prototype.isShowning = function () {
              return this.picker && 'none' !== this.picker.style.display;
            }),
            (e.prototype.loadPolyfillsForIE11 = function () {
              Object.entries ||
                (Object.entries = function (t) {
                  for (var e = Object.keys(t), i = e.length, o = new Array(i); i; ) o[(i -= 1)] = [e[i], t[e[i]]];
                  return o;
                }),
                Element.prototype.matches ||
                  (Element.prototype.matches = Element.prototype.msMatchesSelector || Element.prototype.webkitMatchesSelector),
                Element.prototype.closest ||
                  (Element.prototype.closest = function (t) {
                    var e = this;
                    do {
                      if (e.matches(t)) return e;
                      e = e.parentElement || e.parentNode;
                    } while (null !== e && 1 === e.nodeType);
                    return null;
                  });
            }),
            e
          );
        })(a.Calendar);
      e.Litepicker = h;
    },
    function (t, e, i) {
      'use strict';
      Object.defineProperty(e, '__esModule', { value: !0 });
      var o = i(3);
      (e.Litepicker = o.Litepicker), i(9), i(10), (window.Litepicker = o.Litepicker), (e.default = o.Litepicker);
    },
    function (t, e, i) {
      'use strict';
      var o =
        (this && this.__importStar) ||
        function (t) {
          if (t && t.__esModule) return t;
          var e = {};
          if (null != t) for (var i in t) Object.hasOwnProperty.call(t, i) && (e[i] = t[i]);
          return (e.default = t), e;
        };
      Object.defineProperty(e, '__esModule', { value: !0 });
      var n = i(0),
        s = o(i(1)),
        r = i(2),
        a = (function () {
          function t() {
            (this.options = {
              element: null,
              elementEnd: null,
              parentEl: null,
              firstDay: 1,
              format: 'YYYY-MM-DD',
              lang: 'en-US',
              delimiter: ' - ',
              numberOfMonths: 1,
              numberOfColumns: 1,
              startDate: null,
              endDate: null,
              zIndex: 9999,
              minDate: null,
              maxDate: null,
              minDays: null,
              maxDays: null,
              selectForward: !1,
              selectBackward: !1,
              splitView: !1,
              inlineMode: !1,
              singleMode: !0,
              autoApply: !0,
              allowRepick: !1,
              showWeekNumbers: !1,
              showTooltip: !0,
              hotelMode: !1,
              disableWeekends: !1,
              scrollToDate: !0,
              mobileFriendly: !0,
              useResetBtn: !1,
              autoRefresh: !1,
              moveByOneMonth: !1,
              lockDaysFormat: 'YYYY-MM-DD',
              lockDays: [],
              disallowLockDaysInRange: !1,
              lockDaysInclusivity: '[]',
              bookedDaysFormat: 'YYYY-MM-DD',
              bookedDays: [],
              disallowBookedDaysInRange: !1,
              bookedDaysInclusivity: '[]',
              anyBookedDaysAsCheckout: !1,
              highlightedDaysFormat: 'YYYY-MM-DD',
              highlightedDays: [],
              dropdowns: { minYear: 1990, maxYear: null, months: !1, years: !1 },
              buttonText: {
                apply: 'Apply',
                cancel: 'Cancel',
                previousMonth:
                  '<svg width="11" height="16" xmlns="http://www.w3.org/2000/svg"><path d="M7.919 0l2.748 2.667L5.333 8l5.334 5.333L7.919 16 0 8z" fill-rule="nonzero"/></svg>',
                nextMonth:
                  '<svg width="11" height="16" xmlns="http://www.w3.org/2000/svg"><path d="M2.748 16L0 13.333 5.333 8 0 2.667 2.748 0l7.919 8z" fill-rule="nonzero"/></svg>',
                reset:
                  '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 0 24 24" width="24">\n        <path d="M0 0h24v24H0z" fill="none"/>\n        <path d="M13 3c-4.97 0-9 4.03-9 9H1l3.89 3.89.07.14L9 12H6c0-3.87 3.13-7 7-7s7 3.13 7 7-3.13 7-7 7c-1.93 0-3.68-.79-4.94-2.06l-1.42 1.42C8.27 19.99 10.51 21 13 21c4.97 0 9-4.03 9-9s-4.03-9-9-9zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8H12z"/>\n      </svg>',
              },
              tooltipText: { one: 'day', other: 'days' },
              tooltipPluralSelector: null,
              onShow: null,
              onHide: null,
              onSelect: null,
              onError: null,
              onRender: null,
              onRenderDay: null,
              onChangeMonth: null,
              onChangeYear: null,
              onDayHover: null,
              onShowTooltip: null,
              resetBtnCallback: null,
              moduleRanges: null,
              moduleNavKeyboard: null,
            }),
              (this.calendars = []),
              (this.datePicked = []);
          }
          return (
            (t.prototype.render = function () {
              var t = this,
                e = document.createElement('div');
              e.className = s.containerMain;
              var i = document.createElement('div');
              (i.className = s.containerMonths),
                s['columns' + this.options.numberOfColumns] &&
                  (i.classList.remove(s.columns2, s.columns3, s.columns4), i.classList.add(s['columns' + this.options.numberOfColumns])),
                this.options.splitView && i.classList.add(s.splitView),
                this.options.showWeekNumbers && i.classList.add(s.showWeekNumbers);
              for (
                var o = this.calendars[0].clone(), n = o.getMonth(), r = o.getMonth() + this.options.numberOfMonths, a = 0, l = n;
                l < r;
                l += 1
              ) {
                var p = o.clone();
                p.setDate(1),
                  this.options.splitView ? (p = this.calendars[a].clone()) : p.setMonth(l),
                  i.appendChild(this.renderMonth(p)),
                  (a += 1);
              }
              if (((this.picker.innerHTML = ''), e.appendChild(i), this.options.useResetBtn)) {
                var c = document.createElement('a');
                (c.href = '#'),
                  (c.className = s.resetButton),
                  (c.innerHTML = this.options.buttonText.reset),
                  c.addEventListener('click', function (e) {
                    e.preventDefault(),
                      t.clearSelection(),
                      'function' == typeof t.options.resetBtnCallback && t.options.resetBtnCallback.call(t);
                  }),
                  e
                    .querySelector('.' + s.monthItem + ':last-child')
                    .querySelector('.' + s.monthItemHeader)
                    .appendChild(c);
              }
              if (
                (this.picker.appendChild(e),
                (this.options.autoApply && !this.options.footerHTML) || this.picker.appendChild(this.renderFooter()),
                this.options.showTooltip && this.picker.appendChild(this.renderTooltip()),
                this.options.moduleRanges)
              ) {
                if ('function' != typeof this.enableModuleRanges)
                  throw new Error(
                    'moduleRanges is on but library does not included. See https://github.com/wakirin/litepicker-module-ranges.'
                  );
                this.enableModuleRanges.call(this, this);
              }
              'function' == typeof this.options.onRender && this.options.onRender.call(this, this.picker);
            }),
            (t.prototype.renderMonth = function (t) {
              var e = this,
                i = t.clone(),
                o = 32 - new Date(i.getFullYear(), i.getMonth(), 32).getDate(),
                a = document.createElement('div');
              a.className = s.monthItem;
              var l = document.createElement('div');
              l.className = s.monthItemHeader;
              var p = document.createElement('div');
              if (this.options.dropdowns.months) {
                var c = document.createElement('select');
                c.className = s.monthItemName;
                for (var h = 0; h < 12; h += 1) {
                  var d = document.createElement('option'),
                    u = new n.DateTime(new Date(t.getFullYear(), h, 1, 0, 0, 0));
                  (d.value = String(h)),
                    (d.text = u.toLocaleString(this.options.lang, { month: 'long' })),
                    (d.disabled =
                      (this.options.minDate && u.isBefore(new n.DateTime(this.options.minDate), 'month')) ||
                      (this.options.maxDate && u.isAfter(new n.DateTime(this.options.maxDate), 'month'))),
                    (d.selected = u.getMonth() === t.getMonth()),
                    c.appendChild(d);
                }
                c.addEventListener('change', function (t) {
                  var i = t.target,
                    o = 0;
                  if (e.options.splitView) {
                    var n = i.closest('.' + s.monthItem);
                    o = r.findNestedMonthItem(n);
                  }
                  e.calendars[o].setMonth(Number(i.value)),
                    e.render(),
                    'function' == typeof e.options.onChangeMonth && e.options.onChangeMonth.call(e, e.calendars[o], o);
                }),
                  p.appendChild(c);
              } else {
                var m = document.createElement('strong');
                (m.className = s.monthItemName), (m.innerHTML = t.toLocaleString(this.options.lang, { month: 'long' })), p.appendChild(m);
              }
              if (this.options.dropdowns.years) {
                var f = document.createElement('select');
                f.className = s.monthItemYear;
                var g = this.options.dropdowns.minYear,
                  y = this.options.dropdowns.maxYear ? this.options.dropdowns.maxYear : new Date().getFullYear();
                if (t.getFullYear() > y)
                  ((d = document.createElement('option')).value = String(t.getFullYear())),
                    (d.text = String(t.getFullYear())),
                    (d.selected = !0),
                    (d.disabled = !0),
                    f.appendChild(d);
                for (h = y; h >= g; h -= 1) {
                  var d = document.createElement('option'),
                    k = new n.DateTime(new Date(h, 0, 1, 0, 0, 0));
                  (d.value = h),
                    (d.text = h),
                    (d.disabled =
                      (this.options.minDate && k.isBefore(new n.DateTime(this.options.minDate), 'year')) ||
                      (this.options.maxDate && k.isAfter(new n.DateTime(this.options.maxDate), 'year'))),
                    (d.selected = t.getFullYear() === h),
                    f.appendChild(d);
                }
                if (t.getFullYear() < g)
                  ((d = document.createElement('option')).value = String(t.getFullYear())),
                    (d.text = String(t.getFullYear())),
                    (d.selected = !0),
                    (d.disabled = !0),
                    f.appendChild(d);
                if ('asc' === this.options.dropdowns.years) {
                  var v = Array.prototype.slice.call(f.childNodes).reverse();
                  (f.innerHTML = ''),
                    v.forEach(function (t) {
                      (t.innerHTML = t.value), f.appendChild(t);
                    });
                }
                f.addEventListener('change', function (t) {
                  var i = t.target,
                    o = 0;
                  if (e.options.splitView) {
                    var n = i.closest('.' + s.monthItem);
                    o = r.findNestedMonthItem(n);
                  }
                  e.calendars[o].setFullYear(Number(i.value)),
                    e.render(),
                    'function' == typeof e.options.onChangeYear && e.options.onChangeYear.call(e, e.calendars[o], o);
                }),
                  p.appendChild(f);
              } else {
                var D = document.createElement('span');
                (D.className = s.monthItemYear), (D.innerHTML = String(t.getFullYear())), p.appendChild(D);
              }
              var b = document.createElement('a');
              (b.href = '#'), (b.className = s.buttonPreviousMonth), (b.innerHTML = this.options.buttonText.previousMonth);
              var w = document.createElement('a');
              (w.href = '#'),
                (w.className = s.buttonNextMonth),
                (w.innerHTML = this.options.buttonText.nextMonth),
                l.appendChild(b),
                l.appendChild(p),
                l.appendChild(w),
                this.options.minDate &&
                  i.isSameOrBefore(new n.DateTime(this.options.minDate), 'month') &&
                  a.classList.add(s.noPreviousMonth),
                this.options.maxDate && i.isSameOrAfter(new n.DateTime(this.options.maxDate), 'month') && a.classList.add(s.noNextMonth);
              var M = document.createElement('div');
              (M.className = s.monthItemWeekdaysRow), this.options.showWeekNumbers && (M.innerHTML = '<div>W</div>');
              for (var x = 1; x <= 7; x += 1) {
                var T = 3 + this.options.firstDay + x,
                  _ = document.createElement('div');
                (_.innerHTML = this.weekdayName(T)), (_.title = this.weekdayName(T, 'long')), M.appendChild(_);
              }
              var L = document.createElement('div');
              L.className = s.containerDays;
              var S = this.calcSkipDays(i);
              this.options.showWeekNumbers && S && L.appendChild(this.renderWeekNumber(i));
              for (var E = 0; E < S; E += 1) {
                var I = document.createElement('div');
                L.appendChild(I);
              }
              for (E = 1; E <= o; E += 1)
                i.setDate(E),
                  this.options.showWeekNumbers && i.getDay() === this.options.firstDay && L.appendChild(this.renderWeekNumber(i)),
                  L.appendChild(this.renderDay(i));
              return a.appendChild(l), a.appendChild(M), a.appendChild(L), a;
            }),
            (t.prototype.renderDay = function (t) {
              var e = this;
              t.setHours();
              var i = document.createElement('a');
              if (
                ((i.href = '#'),
                (i.className = s.dayItem),
                (i.innerHTML = String(t.getDate())),
                (i.dataset.time = String(t.getTime())),
                t.toDateString() === new Date().toDateString() && i.classList.add(s.isToday),
                this.datePicked.length
                  ? (this.datePicked[0].toDateString() === t.toDateString() &&
                      (i.classList.add(s.isStartDate), this.options.singleMode && i.classList.add(s.isEndDate)),
                    2 === this.datePicked.length && this.datePicked[1].toDateString() === t.toDateString() && i.classList.add(s.isEndDate),
                    2 === this.datePicked.length && t.isBetween(this.datePicked[0], this.datePicked[1]) && i.classList.add(s.isInRange))
                  : this.options.startDate &&
                    (this.options.startDate.toDateString() === t.toDateString() &&
                      (i.classList.add(s.isStartDate), this.options.singleMode && i.classList.add(s.isEndDate)),
                    this.options.endDate && this.options.endDate.toDateString() === t.toDateString() && i.classList.add(s.isEndDate),
                    this.options.startDate &&
                      this.options.endDate &&
                      t.isBetween(this.options.startDate, this.options.endDate) &&
                      i.classList.add(s.isInRange)),
                this.options.minDate && t.isBefore(new n.DateTime(this.options.minDate)) && i.classList.add(s.isLocked),
                this.options.maxDate && t.isAfter(new n.DateTime(this.options.maxDate)) && i.classList.add(s.isLocked),
                this.options.minDays && 1 === this.datePicked.length)
              ) {
                var o = Number(!this.options.hotelMode),
                  r = this.datePicked[0].clone().subtract(this.options.minDays - o, 'day'),
                  a = this.datePicked[0].clone().add(this.options.minDays - o, 'day');
                t.isBetween(r, this.datePicked[0], '(]') && i.classList.add(s.isLocked),
                  t.isBetween(this.datePicked[0], a, '[)') && i.classList.add(s.isLocked);
              }
              if (this.options.maxDays && 1 === this.datePicked.length) {
                (o = Number(this.options.hotelMode)),
                  (r = this.datePicked[0].clone().subtract(this.options.maxDays + o, 'day')),
                  (a = this.datePicked[0].clone().add(this.options.maxDays + o, 'day'));
                t.isSameOrBefore(r) && i.classList.add(s.isLocked), t.isSameOrAfter(a) && i.classList.add(s.isLocked);
              }
              (this.options.selectForward && 1 === this.datePicked.length && t.isBefore(this.datePicked[0]) && i.classList.add(s.isLocked),
              this.options.selectBackward && 1 === this.datePicked.length && t.isAfter(this.datePicked[0]) && i.classList.add(s.isLocked),
              this.options.lockDays.length) &&
                this.options.lockDays.filter(function (i) {
                  return i instanceof Array ? t.isBetween(i[0], i[1], e.options.lockDaysInclusivity) : i.isSame(t, 'day');
                }).length &&
                i.classList.add(s.isLocked);
              this.options.highlightedDays.length &&
                this.options.highlightedDays.filter(function (e) {
                  return e instanceof Array ? t.isBetween(e[0], e[1], '[]') : e.isSame(t, 'day');
                }).length &&
                i.classList.add(s.isHighlighted);
              if (this.datePicked.length <= 1 && this.options.bookedDays.length) {
                var l = this.options.bookedDaysInclusivity;
                this.options.hotelMode && 1 === this.datePicked.length && (l = '()');
                var p = t.clone();
                p.subtract(1, 'day'), t.clone().add(1, 'day');
                var c = this.dateIsBooked(t, l),
                  h = this.dateIsBooked(p, '[]'),
                  d = this.dateIsBooked(t, '(]'),
                  u =
                    (0 === this.datePicked.length && c) ||
                    (1 === this.datePicked.length && h && c) ||
                    (1 === this.datePicked.length && h && d),
                  m = this.options.anyBookedDaysAsCheckout && 1 === this.datePicked.length;
                u && !m && i.classList.add(s.isBooked);
              }
              return (
                !this.options.disableWeekends || (6 !== t.getDay() && 0 !== t.getDay()) || i.classList.add(s.isLocked),
                'function' == typeof this.options.onRenderDay && this.options.onRenderDay.call(this, i),
                i
              );
            }),
            (t.prototype.renderFooter = function () {
              var t = document.createElement('div');
              if (
                ((t.className = s.containerFooter),
                this.options.footerHTML
                  ? (t.innerHTML = this.options.footerHTML)
                  : (t.innerHTML =
                      '\n      <span class="' +
                      s.previewDateRange +
                      '"></span>\n      <button type="button" class="' +
                      s.buttonCancel +
                      '">' +
                      this.options.buttonText.cancel +
                      '</button>\n      <button type="button" class="' +
                      s.buttonApply +
                      '">' +
                      this.options.buttonText.apply +
                      '</button>\n      '),
                this.options.singleMode)
              ) {
                if (1 === this.datePicked.length) {
                  var e = this.datePicked[0].format(this.options.format, this.options.lang);
                  t.querySelector('.' + s.previewDateRange).innerHTML = e;
                }
              } else if (
                (1 === this.datePicked.length && t.querySelector('.' + s.buttonApply).setAttribute('disabled', ''),
                2 === this.datePicked.length)
              ) {
                e = this.datePicked[0].format(this.options.format, this.options.lang);
                var i = this.datePicked[1].format(this.options.format, this.options.lang);
                t.querySelector('.' + s.previewDateRange).innerHTML = '' + e + this.options.delimiter + i;
              }
              return t;
            }),
            (t.prototype.renderWeekNumber = function (t) {
              var e = document.createElement('div'),
                i = t.getWeek(this.options.firstDay);
              return (e.className = s.weekNumber), (e.innerHTML = 53 === i && 0 === t.getMonth() ? '53 / 1' : i), e;
            }),
            (t.prototype.renderTooltip = function () {
              var t = document.createElement('div');
              return (t.className = s.containerTooltip), t;
            }),
            (t.prototype.dateIsBooked = function (t, e) {
              return this.options.bookedDays.filter(function (i) {
                return i instanceof Array ? t.isBetween(i[0], i[1], e) : i.isSame(t, 'day');
              }).length;
            }),
            (t.prototype.weekdayName = function (t, e) {
              return void 0 === e && (e = 'short'), new Date(1970, 0, t, 12, 0, 0, 0).toLocaleString(this.options.lang, { weekday: e });
            }),
            (t.prototype.calcSkipDays = function (t) {
              var e = t.getDay() - this.options.firstDay;
              return e < 0 && (e += 7), e;
            }),
            t
          );
        })();
      e.Calendar = a;
    },
    function (t, e, i) {
      (e = t.exports = i(7)(!1)).push([
        t.i,
        ':root{--litepickerBgColor: #fff;--litepickerMonthHeaderTextColor: #333;--litepickerMonthButton: #9e9e9e;--litepickerMonthButtonHover: #2196f3;--litepickerMonthWidth: calc(var(--litepickerDayWidth) * 7);--litepickerMonthWeekdayColor: #9e9e9e;--litepickerDayColor: #333;--litepickerDayColorHover: #2196f3;--litepickerDayIsTodayColor: #f44336;--litepickerDayIsInRange: #bbdefb;--litepickerDayIsLockedColor: #9e9e9e;--litepickerDayIsBookedColor: #9e9e9e;--litepickerDayIsStartColor: #fff;--litepickerDayIsStartBg: #2196f3;--litepickerDayIsEndColor: #fff;--litepickerDayIsEndBg: #2196f3;--litepickerDayWidth: 38px;--litepickerButtonCancelColor: #fff;--litepickerButtonCancelBg: #9e9e9e;--litepickerButtonApplyColor: #fff;--litepickerButtonApplyBg: #2196f3;--litepickerButtonResetBtn: #909090;--litepickerButtonResetBtnHover: #2196f3;--litepickerHighlightedDayColor: #333;--litepickerHighlightedDayBg: #ffeb3b}.show-week-numbers{--litepickerMonthWidth: calc(var(--litepickerDayWidth) * 8)}.litepicker{font-family:-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;font-size:0.8em;display:none}.litepicker .container__main{display:-webkit-box;display:-ms-flexbox;display:flex}.litepicker .container__months{display:-webkit-box;display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;background-color:var(--litepickerBgColor);border-radius:5px;-webkit-box-shadow:0 0 5px #ddd;box-shadow:0 0 5px #ddd;width:calc(var(--litepickerMonthWidth) + 10px);-webkit-box-sizing:content-box;box-sizing:content-box}.litepicker .container__months.columns-2{width:calc((var(--litepickerMonthWidth) * 2) + 20px)}.litepicker .container__months.columns-3{width:calc((var(--litepickerMonthWidth) * 3) + 30px)}.litepicker .container__months.columns-4{width:calc((var(--litepickerMonthWidth) * 4) + 40px)}.litepicker .container__months.split-view .month-item-header .button-previous-month,.litepicker .container__months.split-view .month-item-header .button-next-month{visibility:visible}.litepicker .container__months .month-item{padding:5px;width:var(--litepickerMonthWidth);-webkit-box-sizing:content-box;box-sizing:content-box}.litepicker .container__months .month-item-header{display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-pack:justify;-ms-flex-pack:justify;justify-content:space-between;font-weight:500;padding:10px 5px;text-align:center;-webkit-box-align:center;-ms-flex-align:center;align-items:center;color:var(--litepickerMonthHeaderTextColor)}.litepicker .container__months .month-item-header div{-webkit-box-flex:1;-ms-flex:1;flex:1}.litepicker .container__months .month-item-header div>.month-item-name{margin-right:5px}.litepicker .container__months .month-item-header div>.month-item-year{padding:0}.litepicker .container__months .month-item-header .reset-button{color:var(--litepickerButtonResetBtn)}.litepicker .container__months .month-item-header .reset-button>svg,.litepicker .container__months .month-item-header .reset-button>img{fill:var(--litepickerButtonResetBtn);pointer-events:none}.litepicker .container__months .month-item-header .reset-button:hover{color:var(--litepickerButtonResetBtnHover)}.litepicker .container__months .month-item-header .reset-button:hover>svg{fill:var(--litepickerButtonResetBtnHover)}.litepicker .container__months .month-item-header .button-previous-month,.litepicker .container__months .month-item-header .button-next-month{visibility:hidden;text-decoration:none;color:var(--litepickerMonthButton);padding:3px 5px;border-radius:3px;-webkit-transition:color 0.3s, border 0.3s;transition:color 0.3s, border 0.3s;cursor:default}.litepicker .container__months .month-item-header .button-previous-month>svg,.litepicker .container__months .month-item-header .button-previous-month>img,.litepicker .container__months .month-item-header .button-next-month>svg,.litepicker .container__months .month-item-header .button-next-month>img{fill:var(--litepickerMonthButton);pointer-events:none}.litepicker .container__months .month-item-header .button-previous-month:hover,.litepicker .container__months .month-item-header .button-next-month:hover{color:var(--litepickerMonthButtonHover)}.litepicker .container__months .month-item-header .button-previous-month:hover>svg,.litepicker .container__months .month-item-header .button-next-month:hover>svg{fill:var(--litepickerMonthButtonHover)}.litepicker .container__months .month-item-weekdays-row{display:-webkit-box;display:-ms-flexbox;display:flex;justify-self:center;-webkit-box-pack:start;-ms-flex-pack:start;justify-content:flex-start;color:var(--litepickerMonthWeekdayColor)}.litepicker .container__months .month-item-weekdays-row>div{padding:5px 0;font-size:85%;-webkit-box-flex:1;-ms-flex:1;flex:1;width:var(--litepickerDayWidth);text-align:center}.litepicker .container__months .month-item:first-child .button-previous-month{visibility:visible}.litepicker .container__months .month-item:last-child .button-next-month{visibility:visible}.litepicker .container__months .month-item.no-previous-month .button-previous-month{visibility:hidden}.litepicker .container__months .month-item.no-next-month .button-next-month{visibility:hidden}.litepicker .container__days{display:-webkit-box;display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;justify-self:center;-webkit-box-pack:start;-ms-flex-pack:start;justify-content:flex-start;text-align:center;-webkit-box-sizing:content-box;box-sizing:content-box}.litepicker .container__days>div,.litepicker .container__days>a{padding:5px 0;width:var(--litepickerDayWidth)}.litepicker .container__days .day-item{color:var(--litepickerDayColor);text-align:center;text-decoration:none;border-radius:3px;-webkit-transition:color 0.3s, border 0.3s;transition:color 0.3s, border 0.3s;cursor:default}.litepicker .container__days .day-item:hover{color:var(--litepickerDayColorHover);-webkit-box-shadow:inset 0 0 0 1px var(--litepickerDayColorHover);box-shadow:inset 0 0 0 1px var(--litepickerDayColorHover)}.litepicker .container__days .day-item.is-today{color:var(--litepickerDayIsTodayColor)}.litepicker .container__days .day-item.is-locked{color:var(--litepickerDayIsLockedColor)}.litepicker .container__days .day-item.is-locked:hover{color:var(--litepickerDayIsLockedColor);-webkit-box-shadow:none;box-shadow:none;cursor:default}.litepicker .container__days .day-item.is-booked{color:var(--litepickerDayIsBookedColor)}.litepicker .container__days .day-item.is-booked:hover{color:var(--litepickerDayIsBookedColor);-webkit-box-shadow:none;box-shadow:none;cursor:default}.litepicker .container__days .day-item.is-in-range{background-color:var(--litepickerDayIsInRange);border-radius:0}.litepicker .container__days .day-item.is-start-date{color:var(--litepickerDayIsStartColor);background-color:var(--litepickerDayIsStartBg);border-top-left-radius:5px;border-bottom-left-radius:5px;border-top-right-radius:0;border-bottom-right-radius:0}.litepicker .container__days .day-item.is-start-date.is-flipped{border-top-left-radius:0;border-bottom-left-radius:0;border-top-right-radius:5px;border-bottom-right-radius:5px}.litepicker .container__days .day-item.is-end-date{color:var(--litepickerDayIsEndColor);background-color:var(--litepickerDayIsEndBg);border-top-left-radius:0;border-bottom-left-radius:0;border-top-right-radius:5px;border-bottom-right-radius:5px}.litepicker .container__days .day-item.is-end-date.is-flipped{border-top-left-radius:5px;border-bottom-left-radius:5px;border-top-right-radius:0;border-bottom-right-radius:0}.litepicker .container__days .day-item.is-start-date.is-end-date{border-top-left-radius:5px;border-bottom-left-radius:5px;border-top-right-radius:5px;border-bottom-right-radius:5px}.litepicker .container__days .day-item.is-highlighted{color:var(--litepickerHighlightedDayColor);background-color:var(--litepickerHighlightedDayBg)}.litepicker .container__days .week-number{display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-align:center;-ms-flex-align:center;align-items:center;-webkit-box-pack:center;-ms-flex-pack:center;justify-content:center;color:#9e9e9e;font-size:85%}.litepicker .container__footer{text-align:right;padding:10px 5px;margin:0 5px;background-color:#fafafa;-webkit-box-shadow:inset 0px 3px 3px 0px #ddd;box-shadow:inset 0px 3px 3px 0px #ddd;border-bottom-left-radius:5px;border-bottom-right-radius:5px}.litepicker .container__footer .preview-date-range{margin-right:10px;font-size:90%}.litepicker .container__footer .button-cancel{background-color:var(--litepickerButtonCancelBg);color:var(--litepickerButtonCancelColor);border:0;padding:3px 7px 4px;border-radius:3px}.litepicker .container__footer .button-cancel>svg,.litepicker .container__footer .button-cancel>img{pointer-events:none}.litepicker .container__footer .button-apply{background-color:var(--litepickerButtonApplyBg);color:var(--litepickerButtonApplyColor);border:0;padding:3px 7px 4px;border-radius:3px;margin-left:10px;margin-right:10px}.litepicker .container__footer .button-apply:disabled{opacity:0.7}.litepicker .container__footer .button-apply>svg,.litepicker .container__footer .button-apply>img{pointer-events:none}.litepicker .container__tooltip{position:absolute;margin-top:-4px;padding:4px 8px;border-radius:4px;background-color:#fff;-webkit-box-shadow:0 1px 3px rgba(0,0,0,0.25);box-shadow:0 1px 3px rgba(0,0,0,0.25);white-space:nowrap;font-size:11px;pointer-events:none;visibility:hidden}.litepicker .container__tooltip:before{position:absolute;bottom:-5px;left:calc(50% - 5px);border-top:5px solid rgba(0,0,0,0.12);border-right:5px solid transparent;border-left:5px solid transparent;content:""}.litepicker .container__tooltip:after{position:absolute;bottom:-4px;left:calc(50% - 4px);border-top:4px solid #fff;border-right:4px solid transparent;border-left:4px solid transparent;content:""}.litepicker-open{overflow:hidden}.litepicker-backdrop{display:none;background-color:#000;opacity:0.3;position:fixed;top:0;right:0;bottom:0;left:0}\n',
        '',
      ]),
        (e.locals = {
          showWeekNumbers: 'show-week-numbers',
          litepicker: 'litepicker',
          containerMain: 'container__main',
          containerMonths: 'container__months',
          columns2: 'columns-2',
          columns3: 'columns-3',
          columns4: 'columns-4',
          splitView: 'split-view',
          monthItemHeader: 'month-item-header',
          buttonPreviousMonth: 'button-previous-month',
          buttonNextMonth: 'button-next-month',
          monthItem: 'month-item',
          monthItemName: 'month-item-name',
          monthItemYear: 'month-item-year',
          resetButton: 'reset-button',
          monthItemWeekdaysRow: 'month-item-weekdays-row',
          noPreviousMonth: 'no-previous-month',
          noNextMonth: 'no-next-month',
          containerDays: 'container__days',
          dayItem: 'day-item',
          isToday: 'is-today',
          isLocked: 'is-locked',
          isBooked: 'is-booked',
          isInRange: 'is-in-range',
          isStartDate: 'is-start-date',
          isFlipped: 'is-flipped',
          isEndDate: 'is-end-date',
          isHighlighted: 'is-highlighted',
          weekNumber: 'week-number',
          containerFooter: 'container__footer',
          previewDateRange: 'preview-date-range',
          buttonCancel: 'button-cancel',
          buttonApply: 'button-apply',
          containerTooltip: 'container__tooltip',
          litepickerOpen: 'litepicker-open',
          litepickerBackdrop: 'litepicker-backdrop',
        });
    },
    function (t, e, i) {
      'use strict';
      t.exports = function (t) {
        var e = [];
        return (
          (e.toString = function () {
            return this.map(function (e) {
              var i = (function (t, e) {
                var i = t[1] || '',
                  o = t[3];
                if (!o) return i;
                if (e && 'function' == typeof btoa) {
                  var n =
                      ((r = o),
                      (a = btoa(unescape(encodeURIComponent(JSON.stringify(r))))),
                      (l = 'sourceMappingURL=data:application/json;charset=utf-8;base64,'.concat(a)),
                      '/*# '.concat(l, ' */')),
                    s = o.sources.map(function (t) {
                      return '/*# sourceURL='.concat(o.sourceRoot).concat(t, ' */');
                    });
                  return [i].concat(s).concat([n]).join('\n');
                }
                var r, a, l;
                return [i].join('\n');
              })(e, t);
              return e[2] ? '@media '.concat(e[2], '{').concat(i, '}') : i;
            }).join('');
          }),
          (e.i = function (t, i) {
            'string' == typeof t && (t = [[null, t, '']]);
            for (var o = {}, n = 0; n < this.length; n++) {
              var s = this[n][0];
              null != s && (o[s] = !0);
            }
            for (var r = 0; r < t.length; r++) {
              var a = t[r];
              (null != a[0] && o[a[0]]) || (i && !a[2] ? (a[2] = i) : i && (a[2] = '('.concat(a[2], ') and (').concat(i, ')')), e.push(a));
            }
          }),
          e
        );
      };
    },
    function (t, e, i) {
      'use strict';
      var o,
        n = {},
        s = function () {
          return void 0 === o && (o = Boolean(window && document && document.all && !window.atob)), o;
        },
        r = (function () {
          var t = {};
          return function (e) {
            if (void 0 === t[e]) {
              var i = document.querySelector(e);
              if (window.HTMLIFrameElement && i instanceof window.HTMLIFrameElement)
                try {
                  i = i.contentDocument.head;
                } catch (t) {
                  i = null;
                }
              t[e] = i;
            }
            return t[e];
          };
        })();
      function a(t, e) {
        for (var i = [], o = {}, n = 0; n < t.length; n++) {
          var s = t[n],
            r = e.base ? s[0] + e.base : s[0],
            a = { css: s[1], media: s[2], sourceMap: s[3] };
          o[r] ? o[r].parts.push(a) : i.push((o[r] = { id: r, parts: [a] }));
        }
        return i;
      }
      function l(t, e) {
        for (var i = 0; i < t.length; i++) {
          var o = t[i],
            s = n[o.id],
            r = 0;
          if (s) {
            for (s.refs++; r < s.parts.length; r++) s.parts[r](o.parts[r]);
            for (; r < o.parts.length; r++) s.parts.push(g(o.parts[r], e));
          } else {
            for (var a = []; r < o.parts.length; r++) a.push(g(o.parts[r], e));
            n[o.id] = { id: o.id, refs: 1, parts: a };
          }
        }
      }
      function p(t) {
        var e = document.createElement('style');
        if (void 0 === t.attributes.nonce) {
          var o = i.nc;
          o && (t.attributes.nonce = o);
        }
        if (
          (Object.keys(t.attributes).forEach(function (i) {
            e.setAttribute(i, t.attributes[i]);
          }),
          'function' == typeof t.insert)
        )
          t.insert(e);
        else {
          var n = r(t.insert || 'head');
          if (!n)
            throw new Error("Couldn't find a style target. This probably means that the value for the 'insert' parameter is invalid.");
          n.appendChild(e);
        }
        return e;
      }
      var c,
        h =
          ((c = []),
          function (t, e) {
            return (c[t] = e), c.filter(Boolean).join('\n');
          });
      function d(t, e, i, o) {
        var n = i ? '' : o.css;
        if (t.styleSheet) t.styleSheet.cssText = h(e, n);
        else {
          var s = document.createTextNode(n),
            r = t.childNodes;
          r[e] && t.removeChild(r[e]), r.length ? t.insertBefore(s, r[e]) : t.appendChild(s);
        }
      }
      function u(t, e, i) {
        var o = i.css,
          n = i.media,
          s = i.sourceMap;
        if (
          (n && t.setAttribute('media', n),
          s &&
            btoa &&
            (o += '\n/*# sourceMappingURL=data:application/json;base64,'.concat(
              btoa(unescape(encodeURIComponent(JSON.stringify(s)))),
              ' */'
            )),
          t.styleSheet)
        )
          t.styleSheet.cssText = o;
        else {
          for (; t.firstChild; ) t.removeChild(t.firstChild);
          t.appendChild(document.createTextNode(o));
        }
      }
      var m = null,
        f = 0;
      function g(t, e) {
        var i, o, n;
        if (e.singleton) {
          var s = f++;
          (i = m || (m = p(e))), (o = d.bind(null, i, s, !1)), (n = d.bind(null, i, s, !0));
        } else
          (i = p(e)),
            (o = u.bind(null, i, e)),
            (n = function () {
              !(function (t) {
                if (null === t.parentNode) return !1;
                t.parentNode.removeChild(t);
              })(i);
            });
        return (
          o(t),
          function (e) {
            if (e) {
              if (e.css === t.css && e.media === t.media && e.sourceMap === t.sourceMap) return;
              o((t = e));
            } else n();
          }
        );
      }
      t.exports = function (t, e) {
        ((e = e || {}).attributes = 'object' == typeof e.attributes ? e.attributes : {}),
          e.singleton || 'boolean' == typeof e.singleton || (e.singleton = s());
        var i = a(t, e);
        return (
          l(i, e),
          function (t) {
            for (var o = [], s = 0; s < i.length; s++) {
              var r = i[s],
                p = n[r.id];
              p && (p.refs--, o.push(p));
            }
            t && l(a(t, e), e);
            for (var c = 0; c < o.length; c++) {
              var h = o[c];
              if (0 === h.refs) {
                for (var d = 0; d < h.parts.length; d++) h.parts[d]();
                delete n[h.id];
              }
            }
          }
        );
      };
    },
    function (t, e, i) {
      'use strict';
      var o =
          (this && this.__assign) ||
          function () {
            return (o =
              Object.assign ||
              function (t) {
                for (var e, i = 1, o = arguments.length; i < o; i++)
                  for (var n in (e = arguments[i])) Object.prototype.hasOwnProperty.call(e, n) && (t[n] = e[n]);
                return t;
              }).apply(this, arguments);
          },
        n =
          (this && this.__importStar) ||
          function (t) {
            if (t && t.__esModule) return t;
            var e = {};
            if (null != t) for (var i in t) Object.hasOwnProperty.call(t, i) && (e[i] = t[i]);
            return (e.default = t), e;
          };
      Object.defineProperty(e, '__esModule', { value: !0 });
      var s = i(0),
        r = i(3),
        a = n(i(1)),
        l = i(2);
      (r.Litepicker.prototype.show = function (t) {
        void 0 === t && (t = null);
        var e = t || this.options.element;
        if (((this.triggerElement = e), this.options.inlineMode))
          return (
            (this.picker.style.position = 'static'),
            (this.picker.style.display = 'inline-block'),
            (this.picker.style.top = null),
            (this.picker.style.left = null),
            (this.picker.style.bottom = null),
            void (this.picker.style.right = null)
          );
        if (this.options.scrollToDate)
          if (!this.options.startDate || (t && t !== this.options.element)) {
            if (t && this.options.endDate && t === this.options.elementEnd) {
              var i = this.options.endDate.clone();
              i.setDate(1),
                this.options.numberOfMonths > 1 && i.setMonth(i.getMonth() - (this.options.numberOfMonths - 1)),
                (this.calendars[0] = i.clone());
            }
          } else {
            var o = this.options.startDate.clone();
            o.setDate(1), (this.calendars[0] = o.clone());
          }
        if (this.options.mobileFriendly && l.isMobile()) {
          (this.picker.style.position = 'fixed'),
            (this.picker.style.display = 'block'),
            'portrait' === l.getOrientation()
              ? ((this.options.numberOfMonths = 1), (this.options.numberOfColumns = 1))
              : ((this.options.numberOfMonths = 2), (this.options.numberOfColumns = 2)),
            this.render();
          var n = this.picker.getBoundingClientRect();
          return (
            (this.picker.style.top = 'calc(50% - ' + n.height / 2 + 'px)'),
            (this.picker.style.left = 'calc(50% - ' + n.width / 2 + 'px)'),
            (this.picker.style.right = null),
            (this.picker.style.bottom = null),
            (this.picker.style.zIndex = this.options.zIndex),
            (this.backdrop.style.display = 'block'),
            (this.backdrop.style.zIndex = this.options.zIndex - 1),
            document.body.classList.add(a.litepickerOpen),
            'function' == typeof this.options.onShow && this.options.onShow.call(this),
            void (t ? t.blur() : this.options.element.blur())
          );
        }
        this.render(),
          (this.picker.style.position = 'absolute'),
          (this.picker.style.display = 'block'),
          (this.picker.style.zIndex = this.options.zIndex);
        var s = e.getBoundingClientRect(),
          r = this.picker.getBoundingClientRect(),
          p = s.bottom,
          c = s.left,
          h = 0,
          d = 0,
          u = 0,
          m = 0;
        if (this.options.parentEl) {
          var f = this.picker.parentNode.getBoundingClientRect();
          (p -= f.bottom),
            (p += s.height) + r.height > window.innerHeight && s.top - f.top - s.height > 0 && (u = s.top - f.top - s.height),
            (c -= f.left) + r.width > window.innerWidth && s.right - f.right - r.width > 0 && (m = s.right - f.right - r.width);
        } else
          (h = window.scrollX || window.pageXOffset),
            (d = window.scrollY || window.pageYOffset),
            p + r.height > window.innerHeight && s.top - r.height > 0 && (u = s.top - r.height),
            c + r.width > window.innerWidth && s.right - r.width > 0 && (m = s.right - r.width);
        (this.picker.style.top = (u || p) + d + 'px'),
          (this.picker.style.left = (m || c) + h + 'px'),
          (this.picker.style.right = null),
          (this.picker.style.bottom = null),
          'function' == typeof this.options.onShow && this.options.onShow.call(this);
      }),
        (r.Litepicker.prototype.hide = function () {
          this.isShowning() &&
            ((this.datePicked.length = 0),
            this.updateInput(),
            this.options.inlineMode
              ? this.render()
              : ((this.picker.style.display = 'none'),
                'function' == typeof this.options.onHide && this.options.onHide.call(this),
                this.options.mobileFriendly && (document.body.classList.remove(a.litepickerOpen), (this.backdrop.style.display = 'none'))));
        }),
        (r.Litepicker.prototype.getDate = function () {
          return this.getStartDate();
        }),
        (r.Litepicker.prototype.getStartDate = function () {
          return this.options.startDate ? this.options.startDate.clone().getDateInstance() : null;
        }),
        (r.Litepicker.prototype.getEndDate = function () {
          return this.options.endDate ? this.options.endDate.clone().getDateInstance() : null;
        }),
        (r.Litepicker.prototype.setDate = function (t) {
          this.setStartDate(t), 'function' == typeof this.options.onSelect && this.options.onSelect.call(this, this.getDate());
        }),
        (r.Litepicker.prototype.setStartDate = function (t) {
          t && ((this.options.startDate = new s.DateTime(t, this.options.format, this.options.lang)), this.updateInput());
        }),
        (r.Litepicker.prototype.setEndDate = function (t) {
          t &&
            ((this.options.endDate = new s.DateTime(t, this.options.format, this.options.lang)),
            this.options.startDate.getTime() > this.options.endDate.getTime() &&
              ((this.options.endDate = this.options.startDate.clone()),
              (this.options.startDate = new s.DateTime(t, this.options.format, this.options.lang))),
            this.updateInput());
        }),
        (r.Litepicker.prototype.setDateRange = function (t, e) {
          (this.triggerElement = void 0),
            this.setStartDate(t),
            this.setEndDate(e),
            this.updateInput(),
            'function' == typeof this.options.onSelect && this.options.onSelect.call(this, this.getStartDate(), this.getEndDate());
        }),
        (r.Litepicker.prototype.gotoDate = function (t, e) {
          void 0 === e && (e = 0);
          var i = new s.DateTime(t);
          i.setDate(1), (this.calendars[e] = i.clone()), this.render();
        }),
        (r.Litepicker.prototype.setLockDays = function (t) {
          (this.options.lockDays = s.DateTime.convertArray(t, this.options.lockDaysFormat)), this.render();
        }),
        (r.Litepicker.prototype.setBookedDays = function (t) {
          (this.options.bookedDays = s.DateTime.convertArray(t, this.options.bookedDaysFormat)), this.render();
        }),
        (r.Litepicker.prototype.setHighlightedDays = function (t) {
          (this.options.highlightedDays = s.DateTime.convertArray(t, this.options.highlightedDaysFormat)), this.render();
        }),
        (r.Litepicker.prototype.setOptions = function (t) {
          delete t.element,
            delete t.elementEnd,
            delete t.parentEl,
            t.startDate && (t.startDate = new s.DateTime(t.startDate, this.options.format, this.options.lang)),
            t.endDate && (t.endDate = new s.DateTime(t.endDate, this.options.format, this.options.lang));
          var e = o(o({}, this.options.dropdowns), t.dropdowns),
            i = o(o({}, this.options.buttonText), t.buttonText),
            n = o(o({}, this.options.tooltipText), t.tooltipText);
          (this.options = o(o({}, this.options), t)),
            (this.options.dropdowns = o({}, e)),
            (this.options.buttonText = o({}, i)),
            (this.options.tooltipText = o({}, n)),
            !this.options.singleMode ||
              this.options.startDate instanceof s.DateTime ||
              ((this.options.startDate = null), (this.options.endDate = null)),
            this.options.singleMode ||
              (this.options.startDate instanceof s.DateTime && this.options.endDate instanceof s.DateTime) ||
              ((this.options.startDate = null), (this.options.endDate = null));
          for (var r = 0; r < this.options.numberOfMonths; r += 1) {
            var a = this.options.startDate ? this.options.startDate.clone() : new s.DateTime();
            a.setDate(1), a.setMonth(a.getMonth() + r), (this.calendars[r] = a);
          }
          this.options.lockDays.length &&
            (this.options.lockDays = s.DateTime.convertArray(this.options.lockDays, this.options.lockDaysFormat)),
            this.options.bookedDays.length &&
              (this.options.bookedDays = s.DateTime.convertArray(this.options.bookedDays, this.options.bookedDaysFormat)),
            this.options.highlightedDays.length &&
              (this.options.highlightedDays = s.DateTime.convertArray(this.options.highlightedDays, this.options.highlightedDaysFormat)),
            this.render(),
            this.options.inlineMode && this.show(),
            this.updateInput();
        }),
        (r.Litepicker.prototype.clearSelection = function () {
          (this.options.startDate = null),
            (this.options.endDate = null),
            (this.datePicked.length = 0),
            this.updateInput(),
            this.isShowning() && this.render();
        }),
        (r.Litepicker.prototype.destroy = function () {
          this.picker && this.picker.parentNode && (this.picker.parentNode.removeChild(this.picker), (this.picker = null)),
            this.backdrop && this.backdrop.parentNode && this.backdrop.parentNode.removeChild(this.backdrop);
        });
    },
    function (t, e, i) {
      'use strict';
      Object.defineProperty(e, '__esModule', { value: !0 });
    },
  ]).Litepicker;
});
console.warn(
  'Litepicker: version 1.5.7 is deprecated. This file will be removed in future releases. Upgrade to v2: https://github.com/wakirin/Litepicker'
);
